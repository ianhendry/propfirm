package com.gracefl.propfirm.mt4;

import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.gracefl.tradeplus.helpers.json.AccountInformation;
import com.gracefl.propfirm.mt4.connection.Mt4ClientTerminalTradeObject;

public class ZeroMQMT4ConnectionTest { 
	
	private static Random rand = new Random(System.nanoTime());
	
	public static void main(String[] args) throws JsonMappingException, java.io.IOException {
		
		String identity = String.format("%04X-%04X", rand.nextInt(), rand.nextInt());
		ZContext ctx = new ZContext();
		
		try {
			// Create context and socket for PUSH
	        
	        ZMQ.Socket sock = ctx.createSocket(SocketType.PUSH);
	        //sock.setHWM(1);
	        //sock.setIdentity(identity.getBytes(ZMQ.CHARSET));
	        if (sock.connect("tcp://localhost:32768")) {
	        	System.out.println("SUCCESS - PUSH scoket connected");
	        }
	        
	        ZMQ.Socket pullSock = ctx.createSocket(SocketType.PULL);
	        //pullSock.setRcvHWM(1);
	        if (pullSock.connect("tcp://localhost:32769")) {
	        	System.out.println("SUCCESS - PULL scoket connected");
	        }
	        
	        ZMQ.Poller poller = ctx.createPoller(1);
	        poller.register(pullSock, ZMQ.Poller.POLLIN);
	        
	        //String message = "HEARTBEAT";
	        //String message = "GET_ACCOUNT_DETAILS";
	        String message = "TRADE;ALL_TRADE_HISTORY";
	        
	        if (sock.send(message)) {
	        	System.out.println("SUCCESS - message sent " + message);
	        } else {
	        	System.out.println("FAILED to send message " + message);
	        }

	        //try for 5 seconds to get a reply
        	poller.poll(5000); 
            if (poller.pollin(0)) {
            	String response = new String(pullSock.recv(), ZMQ.CHARSET).trim();
            	String myJSONstring = response.replace('\'','"');
            	System.out.println(myJSONstring);
            	
            	ObjectMapper objectMapper = new ObjectMapper();

            	Mt4ClientTerminalTradeObject accountInformation = objectMapper.readValue(myJSONstring, Mt4ClientTerminalTradeObject.class);

            	System.out.println(accountInformation.toString());
            } else {
            	System.out.println("{ERROR Nothing received!}");
            }

			pullSock.close();
        	sock.close();
        	ctx.close();
        	
		} catch (org.zeromq.ZMQException ex) {
           System.out.println(ex.getLocalizedMessage());
        } finally {
        	ctx.destroy();
        }
        
	}

}
