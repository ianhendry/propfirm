package com.gracefl.propfirm.mt4.connection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class Mt4ClientConnectionStartUpManager {

	private final Logger log = LoggerFactory.getLogger(Mt4ClientConnectionStartUpManager.class);
	
	@Autowired
	Mt4ClientConnectionTradeHistoryPuller mt4ClientConnectionTradeHistoryPuller;
	
	@PostConstruct
    public void init() throws Exception {	
						
		// finally open the real time data feed from MT4
    	log.info("Call MT4 Client Application for MT4 trade history");
        Thread pullThread = new Thread(mt4ClientConnectionTradeHistoryPuller);
        pullThread.start();
        
	}
}
