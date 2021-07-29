package com.gracefl.propfirm.mt4.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ResetDailyDrawdownTimerController {
	
	private final static Logger log = LoggerFactory.getLogger(ResetDailyDrawdownTimerController.class);
	
    @Autowired
    ResetDailyDrawdownService resetDailyDrawdownService;
    
    // run at 07:00 Tuesday through Saturday 
    @Scheduled(cron="0 0 7 * * TUE-SAT")
    public void launchJob() throws Exception {
 
    	Thread resetDailyDrawdownThread = new Thread(resetDailyDrawdownService);
    	resetDailyDrawdownThread.start();
    	
        log.debug("Daily drawdown reset job is running......check logs");
    }
}