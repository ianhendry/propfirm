package com.gracefl.propfirm.mt4;

import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracefl.propfirm.mt4.connection.Mt4LiveAccountJSON;

public class ZeroMQMT4ConnectionTestSubscriber { 
	
	private static Random rand = new Random(System.nanoTime());
	
	public static void main(String[] args) throws JsonMappingException, java.io.IOException {
		
		String identity = String.format("%04X-%04X", rand.nextInt(), rand.nextInt());
		ZContext ctx = new ZContext();
		
		try {
			// Create context and socket for PUSH
	        
	        ZMQ.Socket sock = ctx.createSocket(SocketType.PUSH);
	        if (sock.connect("tcp://localhost:32768")) {
	        	System.out.println("SUCCESS - PUSH scoket connected");
	        }
	        
	        ZMQ.Socket subscriber = ctx.createSocket(SocketType.SUB);
	        subscriber.setIdentity(identity.getBytes(ZMQ.CHARSET));
	        
	        // Bind socket
	        subscriber.subscribe("".getBytes());
	        subscriber.connect("tcp://localhost:32770");
	        
	        //String message = "HEARTBEAT";
	        //String message = "GET_ACCOUNT_DETAILS";
	        String message = "TRADE;MONITOR_ACTIVE_TRADES";
	        
	        if (sock.send(message)) {
	        	System.out.println("SUCCESS - message sent " + message);
	        } else {
	        	System.out.println("FAILED to send message " + message);
	        }

	        try {
	            String msg = "";
	            while (!msg.equalsIgnoreCase("END")) {
	                msg = new String(subscriber.recv(0));
	                String myJSONstring = msg.replace('\'','"');
	                System.out.println(myJSONstring);
	                
	                ObjectMapper objectMapper = new ObjectMapper();

	                Mt4LiveAccountJSON accountInformation = objectMapper.readValue(myJSONstring, Mt4LiveAccountJSON.class);
	                System.out.println(accountInformation.getAccountInformation().getTimeStamp());
	            }
	        }
	        catch(org.zeromq.ZMQException ex){
	           // break;
	        } finally {
	        	sock.close();
			    subscriber.close();
			 	ctx.close();
	        }
        	
		} catch (org.zeromq.ZMQException ex) {
           System.out.println(ex.getLocalizedMessage());
        } finally {
        	ctx.destroy();
        }
        
	}

}
