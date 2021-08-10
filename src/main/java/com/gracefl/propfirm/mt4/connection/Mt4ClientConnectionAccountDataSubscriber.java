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
import com.gracefl.propfirm.domain.TradeChallenge;
import com.gracefl.propfirm.domain.enumeration.BROKER;
import com.gracefl.propfirm.domain.enumeration.TRADEDIRECTION;
import com.gracefl.propfirm.helpers.DatesHelper;
import com.gracefl.propfirm.mt4.connection.dto.Mt4ClientTerminalTradeObject;
import com.gracefl.propfirm.mt4.connection.dto.Mt4LiveAccountJSON;
import com.gracefl.propfirm.mt4.connection.dto.Mt4TradesObject;
import com.gracefl.propfirm.repository.InstrumentRepository;
import com.gracefl.propfirm.repository.Mt4AccountRepository;
import com.gracefl.propfirm.repository.Mt4TradeRepository;
import com.gracefl.propfirm.repository.TradeChallengeRepository;
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
public class Mt4ClientConnectionAccountDataSubscriber implements Runnable, Closeable {
	
	@Autowired
	private DatesHelper datesHelper;
	
	@Autowired
	private InstrumentRepository instrumentRepository;
	
	@Autowired
	private Mt4AccountRepository mt4AccountRepository;
	
	@Autowired
	private  TradeChallengeRepository tradeChallengeRepository;
	
	@Autowired
	Mt4TradeRepository mt4TradeRepository;

	@Autowired
    private ApplicationProperties forexMarketData;
	
	@Autowired
    private AccountDataTimeSeriesService accountDataTimeSeriesService;
	
	//@Autowired
	//private LiveupdateService liveupdateService;
	
	private static Random rand = new Random(System.nanoTime());
	
	private final static Logger log = LoggerFactory.getLogger(Mt4ClientConnectionAccountDataSubscriber.class);
	
	@Autowired
	private static final ZContext CONTEXT = new ZContext();
	
	private volatile boolean running = true;
    
	private final SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	public Mt4ClientConnectionAccountDataSubscriber(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
    @Override 
    public void run() {
    	
    	// Set up context and socket
    	String identity = String.format("%04X-%04X", rand.nextInt(), rand.nextInt());
    	ZContext ctx = new ZContext();
    	
    	Mt4Account mt4Account = new Mt4Account();
        mt4Account.setAccountBroker(BROKER.FXPRO);
        mt4Account.setInActive(false);
        mt4Account.setAccountLogin("60116818");

		Optional<Mt4Account> mt4AccountsOptional = mt4AccountRepository.findOne(Example.of(mt4Account));
		
		if (mt4AccountsOptional.isPresent()) {
			mt4Account = mt4AccountsOptional.get();
		}
		
    	while(running) {
    		
	    	String mdsAddress = forexMarketData.getforexMarketData().getServer();
	    	int mdsPushSocket = forexMarketData.getforexMarketData().getPushSocket();
	    	int mdsSubscriber = forexMarketData.getforexMarketData().getPubSocket();
	    	
	    	ZMQ.Socket sock = ctx.createSocket(SocketType.PUSH);
	        if (sock.connect(mdsAddress + ":" + mdsPushSocket)) {
	        	log.debug("SUCCESS - PUSH scoket connected {}:{}", mdsAddress, mdsPushSocket);
	        }
	        
	        ZMQ.Socket subscriber = ctx.createSocket(SocketType.SUB);
	        subscriber.setIdentity(identity.getBytes(ZMQ.CHARSET));
	        
	        // Bind socket
	        subscriber.subscribe("".getBytes());
	        subscriber.connect(mdsAddress + ":" + mdsSubscriber);
	        
	        String message = "TRADE;MONITOR_ACTIVE_TRADES";
	        
	        if (sock.send(message)) {
	        	log.debug("SUCCESS - message sent " + message);
	        } else {
	        	log.debug("FAILED to send message " + message);
	        }

	        try {
	            String msg = "";
	            while (!msg.equalsIgnoreCase("END")) {
	                msg = new String(subscriber.recv(0));
	                String myJSONstring = msg.replace('\'','"');
	                
	                log.debug("account info recievd {}", myJSONstring);
	                
	                ObjectMapper objectMapper = new ObjectMapper();
	                
	                if (myJSONstring.contains("ACCOUNT_INFORMATION")) {
	                	Mt4LiveAccountJSON accountInformation = objectMapper.readValue(myJSONstring, Mt4LiveAccountJSON.class);
	                	processAccountDataTimeSeries(accountInformation, mt4Account);
	                } else {
	                	Mt4ClientTerminalTradeObject accountInformation = objectMapper.readValue(myJSONstring, Mt4ClientTerminalTradeObject.class);
	                	processJSONMt4TRades(accountInformation, mt4Account);
	                }
	            }
	        }
	        catch(org.zeromq.ZMQException | JsonProcessingException ex){
	           // break;
	        } finally {
	        	sock.close();
			    subscriber.close();
			 	ctx.close();
	        }
    	}
        
    }

    public void close() throws IOException {
        running = false;
        // Destroy context, which will destroy sockets
        CONTEXT.close();
    }
    
    private void processAccountDataTimeSeries(Mt4LiveAccountJSON processObject, Mt4Account mt4Account) {
		
    	Instant timeStamp = datesHelper.convertStringToInstant(processObject.getAccountInformation().getTimeStamp());
    	Boolean rulesBroken = false;
    	
    	if (!accountDataTimeSeriesRrecordExists(mt4Account, timeStamp)) {
    		
    		// get MT4 values
    		Double equity = processObject.getAccountInformation().getAccountEquity();
    		Double balance = processObject.getAccountInformation().getAccountBalance();
    		Double openProfit = processObject.getAccountInformation().getAccountProfit();
    		Double currentDrawdown;
    		if (openProfit < 0) {
    			currentDrawdown = openProfit;
    		} else {
    			currentDrawdown = 0D;
    		}
    		
	        TradeChallenge challenge = mt4Account.getTradeChallenge();	        
	        Double runningMaxDrawdown = challenge.getRunningMaxTotalDrawdown();
	        Double runningDailyDrawdown = challenge.getRunningMaxDailyDrawdown();
	        Double lastDrawdown = mt4Account.getAccountFreeMarginMode();
	        
	        Double percentChange;
	        
	        if (openProfit > 0 || openProfit < 0) {
	        	percentChange =  lastDrawdown * 100 / currentDrawdown;
	        } else {
	        	percentChange = 0D;
	        }
	        // need to think about how to calculate this daily drawdown 
	        Double dailyDrawdown = mt4Account.getAccountInfoDouble() - currentDrawdown;
	        
	        if (currentDrawdown < runningMaxDrawdown) {
	        	challenge.setRunningMaxTotalDrawdown(currentDrawdown);
	        }
	        if (currentDrawdown < runningDailyDrawdown) {
	        	challenge.setRunningMaxDailyDrawdown(currentDrawdown);
	        }
	        
	        // total drawdown check
	        if (currentDrawdown > challenge.getChallengeType().getMaxTotalDrawDown()) {
	        	challenge.setRunningMaxTotalDrawdown(currentDrawdown);
	        	challenge.setRulesViolated(true);
	        	challenge.setRuleViolatedDate(Instant.now());
	        	challenge.setRuleViolated("Max total drawdown exceeded");
	        	rulesBroken = true;
	        }
	        // daily draw down check
	        if (dailyDrawdown > challenge.getChallengeType().getMaxDailyDrawdown()) {
	        	challenge.setRunningMaxDailyDrawdown(dailyDrawdown);
	        	challenge.setRulesViolated(true);
	        	challenge.setRuleViolatedDate(Instant.now());
	        	challenge.setRuleViolated("Max daily drawdown exceeded");
	        	rulesBroken = true;
	        }
	        
	        // only save if a) rules broken or b) equity has changed by more than 0.01% from last time to limit number records needly saved
	        if (percentChange > 0.01 || rulesBroken) {
	        	
	        	AccountDataTimeSeries accountDataTimeSeries = new AccountDataTimeSeries();
		        accountDataTimeSeries.setAccountBalance(balance);
		        accountDataTimeSeries.setAccountEquity(equity);
		        accountDataTimeSeries.setAccountCredit(processObject.getAccountInformation().getAccountCredit());
		        accountDataTimeSeries.setAccountFreeMargin(processObject.getAccountInformation().getAccountFreeMargin());
		        accountDataTimeSeries.setAccountStopoutLevel(processObject.getAccountInformation().getAccountStopoutLevel().intValue());
		        accountDataTimeSeries.setOpenLots(processObject.getAccountInformation().getOpenLots());
		        accountDataTimeSeries.setOpenNumberOfTrades(processObject.getAccountInformation().getOrdersTotal().intValue());
		        accountDataTimeSeries.setMt4Account(mt4Account);
		        accountDataTimeSeries.setDateStamp(timeStamp);
		        
		        accountDataTimeSeriesService.save(accountDataTimeSeries);
		        
		        mt4Account.setAccountEquity(equity);
		        mt4Account.setAccountBalance(balance);
		        
		       
    		}
    	
	        // used to calculate the % change in draw down each run
	        mt4Account.setAccountFreeMarginMode(currentDrawdown);
	        mt4AccountRepository.save(mt4Account);
	        
	        tradeChallengeRepository.save(challenge);
	        
    	}
	}
    
    private Boolean accountDataTimeSeriesRrecordExists(Mt4Account mt4Account, Instant timeStamp) {
    	
    	AccountDataTimeSeries accountDataTimeSeries = new AccountDataTimeSeries();
    	accountDataTimeSeries.setDateStamp(timeStamp);
    	accountDataTimeSeries.setMt4Account(mt4Account);

    	return false;
    	/*
		Optional<AccountDataTimeSeries> recordOptional = accountDataTimeSeriesService.findOne(Example.of(accountDataTimeSeries));
		
		if (recordOptional.isPresent()) {
			return true;
		} else {
			return false;
		}
		*/
    }
    
 private void processJSONMt4TRades(Mt4ClientTerminalTradeObject processObject, Mt4Account mt4Account) {
		
    	Double balance = mt4Account.getAccountBalance();
    	Double equity = mt4Account.getAccountEquity();
    	
    	for (Mt4TradesObject trade: processObject.getTrades()) {
    		
    		if (trade.getSymbol().length()>1) {
    			
    			//TODO check here if the trade is already stored and do not duplicate 
    			if (!checkMt4TradeExists(trade.getSymbol(), trade.getTicket())) {
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
	    			mt4Trade.setMt4Account(mt4Account);
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
	    	        accountDataTimeSeries.setMt4Account(mt4Account);
	    	        accountDataTimeSeries.setDateStamp(datesHelper.convertStringToInstant(trade.getOpenTime()));
	    	        accountDataTimeSeriesService.save(accountDataTimeSeries);
	    	        
	    	        mt4Account.addAccountDataTimeSeries(accountDataTimeSeries);
	    	        mt4AccountRepository.save(mt4Account);
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
    
	private Boolean checkMt4TradeExists(String symbol, Double ticket ) {
		
		Mt4Trade tradeExample = new Mt4Trade();
		tradeExample.setSymbol(symbol);
		tradeExample.setTicket(BigDecimal.valueOf(ticket));
	
		Optional<Mt4Trade> mt4Trade = mt4TradeRepository.findOne(Example.of(tradeExample));
		
		if (mt4Trade.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

}