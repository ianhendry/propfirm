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
public class Mt4ClientConnectionAccountDataSubscriber implements Runnable, Closeable {
	
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

	                Mt4LiveAccountJSON accountInformation = objectMapper.readValue(myJSONstring, Mt4LiveAccountJSON.class);
	                processAccountDataTimeSeries(accountInformation, mt4Account);
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
    	
    	if (!accountDataTimeSeriesRrecordExists(mt4Account, timeStamp)) {
    		
			AccountDataTimeSeries accountDataTimeSeries = new AccountDataTimeSeries();
	        accountDataTimeSeries.setAccountBalance(processObject.getAccountInformation().getAccountBalance());
	        accountDataTimeSeries.setAccountEquity(processObject.getAccountInformation().getAccountEquity());
	        accountDataTimeSeries.setAccountCredit(processObject.getAccountInformation().getAccountCredit());
	        accountDataTimeSeries.setAccountFreeMargin(processObject.getAccountInformation().getAccountFreeMargin());
	        accountDataTimeSeries.setAccountStopoutLevel(processObject.getAccountInformation().getAccountStopoutLevel().intValue());
	        accountDataTimeSeries.setOpenLots(processObject.getAccountInformation().getOpenLots());
	        accountDataTimeSeries.setOpenNumberOfTrades(processObject.getAccountInformation().getOrdersTotal().intValue());
	        accountDataTimeSeries.setMt4Account(mt4Account);
	        accountDataTimeSeries.setDateStamp(timeStamp);
	        
	        accountDataTimeSeriesService.save(accountDataTimeSeries);
	        
	        //mt4Account.addAccountDataTimeSeries(accountDataTimeSeries);
	        //mt4AccountRepository.save(mt4Account);
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
}