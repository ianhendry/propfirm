package com.gracefl.propfirm.mt4.connection;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracefl.propfirm.config.ApplicationProperties;
import com.gracefl.propfirm.domain.AccountDataTimeSeries;
import com.gracefl.propfirm.domain.Instrument;
import com.gracefl.propfirm.domain.Mt4Account;
import com.gracefl.propfirm.domain.Mt4Trade;
import com.gracefl.propfirm.domain.enumeration.BROKER;
import com.gracefl.propfirm.domain.enumeration.TRADEDIRECTION;
import com.gracefl.propfirm.helpers.DatesHelper;
import com.gracefl.propfirm.repository.InstrumentRepository;
import com.gracefl.propfirm.repository.Mt4AccountRepository;
import com.gracefl.propfirm.repository.Mt4TradeRepository;
import com.gracefl.propfirm.service.AccountDataTimeSeriesService;
import com.gracefl.propfirm.service.Mt4AccountService;

import net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer.ForReturnTypeName;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class Mt4ClientConnectionTradeHistoryPuller implements Runnable, Closeable {
	
	@Autowired
	private DatesHelper datesHelper;
	
	@Autowired
	private InstrumentRepository instrumentRepository;
	
	@Autowired
	private Mt4AccountRepository mt4AccountRepository;
	
	@Autowired
	Mt4TradeRepository mt4TradeRepository;

	@Autowired
    private ApplicationProperties forexMarketData;
	
	@Autowired
    private AccountDataTimeSeriesService accountDataTimeSeriesService;
	
	//@Autowired
	//private LiveupdateService liveupdateService;
	
	private static Random rand = new Random(System.nanoTime());
	
	private final static Logger log = LoggerFactory.getLogger(Mt4ClientConnectionTradeHistoryPuller.class);
	
	@Autowired
	private static final ZContext CONTEXT = new ZContext();
	
	private volatile boolean running = true;
    
	private final SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	public Mt4ClientConnectionTradeHistoryPuller(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
    @Override 
    public void run() {
    	
    	// Set up context and socket
    	String identity = String.format("%04X-%04X", rand.nextInt(), rand.nextInt());
    	ZContext ctx = new ZContext();
    	
    	try { 
	    	String mdsAddress = forexMarketData.getforexMarketData().getServer();
	    	int mdsPullSocket = forexMarketData.getforexMarketData().getPullSocket();
	    	int mdsPushSocket = forexMarketData.getforexMarketData().getPushSocket();
	    	
	    	ZMQ.Socket sock = ctx.createSocket(SocketType.PUSH);
	        //sock.setHWM(1);
	        //sock.setIdentity(identity.getBytes(ZMQ.CHARSET));
	        if (sock.connect(mdsAddress + ":" + mdsPushSocket)) {	//68
	        	System.out.println("SUCCESS - PUSH scoket connected");
	        }
	        
	        ZMQ.Socket pullSock = ctx.createSocket(SocketType.PULL);
	        //pullSock.setRcvHWM(1);
	        if (pullSock.connect(mdsAddress + ":" + mdsPullSocket)) {  //69
	        	System.out.println("SUCCESS - PULL scoket connected");
	        }
	        
	        ZMQ.Poller poller = ctx.createPoller(1);
	        poller.register(pullSock, ZMQ.Poller.POLLIN);
	    	
	        Mt4Account mt4Account = new Mt4Account();
	        mt4Account.setAccountBroker(BROKER.FXPRO);
	        mt4Account.setInActive(false);
	        mt4Account.setAccountLogin("60116818");

			Example<Mt4Account> mt4AccountsExample = Example.of(mt4Account);
			
	        //List<Mt4Account> mt4Accounts = mt4AccountService.findAllByAccountLogin("60116818");
			Optional<Mt4Account> mt4Accounts = mt4AccountRepository.findOne(mt4AccountsExample);
			
			// TODO - check here that only one account is returned
			
	        String pushString = "TRADE;ALL_TRADE_HISTORY";
	
	        log.debug("Requesting from MT4: " + pushString);
	        
	        sock.send(pushString.getBytes(), 0);
	        //try for 5 seconds to get a reply
        	poller.poll(5000); 
            if (poller.pollin(0)) {
            	String response = new String(pullSock.recv(), ZMQ.CHARSET).trim();
            	String myJSONstring = response.replace('\'','"');
            	System.out.println(myJSONstring);
            	
            	ObjectMapper objectMapper = new ObjectMapper();

            	Mt4ClientTerminalTradeObject accountInformation = objectMapper.readValue(myJSONstring, Mt4ClientTerminalTradeObject.class);
            	
            	processJSONMt4TRades(accountInformation, mt4Accounts);
            	
            	System.out.println(accountInformation.toString());
            } else {
            	System.out.println("{ERROR Nothing received!}");
            }

			pullSock.close();
        	sock.close();
        	ctx.close();
        	
		} catch (org.zeromq.ZMQException | JsonProcessingException ex) {
           System.out.println(ex.getLocalizedMessage());
        } finally {
        	ctx.destroy();
        }
        
    }

    public void close() throws IOException {
        running = false;
        // Destroy context, which will destroy sockets
        CONTEXT.close();
    }
    
    private void processJSONMt4TRades(Mt4ClientTerminalTradeObject processObject, Optional<Mt4Account> mt4Account) {
		
    	Mt4Account singleMt4Account = mt4Account.get();
    	Double balance = singleMt4Account.getAccountBalance();
    	Double equity = singleMt4Account.getAccountEquity();
    	
    	for (Mt4TradesObject trade: processObject.getTrades()) {
    		
    		if (trade.getSymbol().length()>1) {
    			
    			//TODO check here if the trade is already stored and do not duplicate 
    			if (!checkMt4TradeExists(singleMt4Account, trade.getTicket())) {
	    			TRADEDIRECTION tradeDirection; 
	    			if (trade.getType() == 1) {
	    				tradeDirection = TRADEDIRECTION.SELL; 
	    			} else {
	    				tradeDirection = TRADEDIRECTION.BUY;
	    			}
	    				
	    			Instrument instrument = getIntrumentFromTicker(trade.getSymbol()); 			
	    			
	    			Mt4Trade mt4Trade = new Mt4Trade();
	    			mt4Trade.setClosePrice(trade.getOpenPrice().doubleValue());
	    			mt4Trade.setCloseTime(datesHelper.convertStringToInstant(trade.getOpenTime()));
	    			mt4Trade.setCommission(trade.getCommission().doubleValue());
	    			mt4Trade.setDirectionType(tradeDirection);
	    			mt4Trade.setInstrument(instrument);
	    			mt4Trade.setMt4Account(singleMt4Account);
	    			mt4Trade.setOpenPrice(trade.getOpenPrice().doubleValue());
	    			mt4Trade.setPositionSize(trade.getLots());
	    			mt4Trade.setStopLossPrice(trade.getStopLoss().doubleValue());
	    			mt4Trade.setSymbol(trade.getSymbol());
	    			mt4Trade.setTakeProfitPrice(trade.getTakeProfit().doubleValue());
	    			mt4Trade.setTicket(BigDecimal.valueOf(trade.getTicket()));
	    			mt4Trade.setSwap(trade.getSwap().doubleValue());
	    			mt4Trade.setProfit(trade.getProfit().doubleValue());
	    			
	    			mt4TradeRepository.save(mt4Trade);
	    			
	    			balance = balance + trade.getProfit().doubleValue();
	    			equity = equity + trade.getProfit().doubleValue();
	    			AccountDataTimeSeries accountDataTimeSeries = new AccountDataTimeSeries();
	    	        accountDataTimeSeries.setAccountBalance(balance);
	    	        accountDataTimeSeries.setAccountEquity(equity);
	    	        accountDataTimeSeries.setMt4Account(singleMt4Account);
	    	        accountDataTimeSeries.setDateStamp(datesHelper.convertStringToInstant(trade.getOpenTime()));
	    	        accountDataTimeSeriesService.save(accountDataTimeSeries);
	    	        
	    	        singleMt4Account.addAccountDataTimeSeries(accountDataTimeSeries);
	    	        mt4AccountRepository.save(singleMt4Account);
    			} else {
    				log.debug("Trade is already in the database. Did not update {} ", trade);
    			}
    		}
    	}
	}
    
    private Instrument getIntrumentFromTicker(String ticker) {
    	Instrument instrument = new Instrument();
		instrument.setTicker(ticker);
		Optional<Instrument> inst = instrumentRepository.findOne(Example.of(instrument));
		
		if (inst.isPresent()) {
			return inst.get();
		} else {
			return null;
		}
		
    }
    
	private Boolean checkMt4TradeExists(Mt4Account singleMt4Account, Double ticket ) {
		
		Mt4Trade tradeExample = new Mt4Trade();
		tradeExample.setMt4Account(singleMt4Account);
		tradeExample.setTicket(BigDecimal.valueOf(ticket));
	
		Optional<Mt4Trade> mt4Accounts = mt4TradeRepository.findOne(Example.of(tradeExample));
		
		if (mt4Accounts.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

}