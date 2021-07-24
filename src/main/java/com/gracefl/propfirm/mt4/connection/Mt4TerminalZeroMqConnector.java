package com.gracefl.propfirm.mt4.connection;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Mt4TerminalZeroMqConnector {

	private final Logger log = LoggerFactory.getLogger(Mt4TerminalZeroMqConnector.class);
	
	@Autowired
	private static final ZContext CONTEXT = new ZContext();
	
	String identity = String.format("%04X-%04X", rand.nextInt(), rand.nextInt());
	//ZContext ctx = new ZContext();
	private static Random rand = new Random(System.nanoTime());

	public Optional<AccountInformation> getAccountInformation(Long accountId) throws JsonMappingException, JsonProcessingException {
		
		Optional<AccountInformation> accountInformation = null;
		
		try {
			// Create context and socket for PUSH
	        
	        ZMQ.Socket sock = CONTEXT.createSocket(SocketType.PUSH);
	        sock.setHWM(1);
	        sock.setIdentity(identity.getBytes(ZMQ.CHARSET));
	        if (sock.connect("tcp://localhost:32768")) {
	        	log.debug("SUCCESS - PUSH scoket connected:\t");
	        }
	        
	        ZMQ.Socket pullSock = CONTEXT.createSocket(SocketType.PULL);
	        pullSock.setRcvHWM(1);
	        if (pullSock.connect("tcp://localhost:32769")) {
	        	log.debug("SUCCESS - PULL scoket connected:\t");
	        }
	        
	        ZMQ.Poller poller = CONTEXT.createPoller(1);
	        poller.register(pullSock, ZMQ.Poller.POLLIN);
	        
	        String message = "GET_ACCOUNT_DETAILS";
	        
	        if (sock.send(message)) {
	        	log.debug("SUCCESS - message sent:\t" + message);
	        } else {
	        	log.debug("FAILED to send message:\t" + message);
	        }
	    
	        //try for 5 seconds to get a reply
	    	poller.poll(5000); 
	        if (poller.pollin(0)) {
	        	String response = new String(pullSock.recv(), ZMQ.CHARSET).trim();
	        	ObjectMapper objectMapper = new ObjectMapper();
	    		
	        	accountInformation = Optional.ofNullable(objectMapper.readValue(response, AccountInformation.class));
	        	
	        	log.debug(accountInformation.toString());
	        } else {
	        	log.debug("{ERROR Nothing received!}");
	        }
				
			//pullSock.close();
	    	//sock.close();
	    	//CONTEXT.close();
	    	
		} catch (org.zeromq.ZMQException ex) {
	       log.error(ex.getLocalizedMessage());           
	    } finally {
	    	//CONTEXT.destroy();
	    }
		return accountInformation;
	}   
	
	
	public Optional<String> getHeartBeat() throws JsonMappingException, JsonProcessingException {
		
		Optional<String> heartBeatResponse = null;
		
		try {
			// Create context and socket for PUSH
	        
	        ZMQ.Socket sock = CONTEXT.createSocket(SocketType.PUSH);
	        sock.setHWM(1);
	        sock.setIdentity(identity.getBytes(ZMQ.CHARSET));
	        if (sock.connect("tcp://localhost:32768")) {
	        	log.debug("SUCCESS - PUSH scoket connected:\t");
	        }
	        
	        ZMQ.Socket pullSock = CONTEXT.createSocket(SocketType.PULL);
	        pullSock.setRcvHWM(1);
	        if (pullSock.connect("tcp://localhost:32769")) {
	        	log.debug("SUCCESS - PULL scoket connected:\t");
	        }
	        
	        ZMQ.Poller poller = CONTEXT.createPoller(1);
	        poller.register(pullSock, ZMQ.Poller.POLLIN);
	        
	        String message = "HEARTBEAT";
	        
	        if (sock.send(message)) {
	        	log.debug("SUCCESS - message sent:\t" + message);
	        } else {
	        	log.debug("FAILED to send message:\t" + message);
	        }
	    
	        //try for 5 seconds to get a reply
	    	poller.poll(5000); 
	        if (poller.pollin(0)) {
	        	String response = new String(pullSock.recv(), ZMQ.CHARSET).trim();
	        	
	        	heartBeatResponse = Optional.ofNullable(response);
	        	//System.out.println(response);

	        } else {
	        	heartBeatResponse = Optional.ofNullable("{ERROR Nothing received in heart beat response!}");;
	        }
				
			//pullSock.close();
	    	//sock.close();
	    	//CONTEXT.close();
	    	
		} catch (org.zeromq.ZMQException ex) {
	       log.error(ex.getLocalizedMessage());           
	    } finally {
	    	//CONTEXT.destroy();
	    }
		return heartBeatResponse;
	}   
	
}
