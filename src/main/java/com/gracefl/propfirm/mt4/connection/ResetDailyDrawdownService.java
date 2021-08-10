package com.gracefl.propfirm.mt4.connection;

import java.io.Closeable;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import com.gracefl.propfirm.domain.Mt4Account;
import com.gracefl.propfirm.domain.TradeChallenge;
import com.gracefl.propfirm.repository.Mt4AccountRepository;
import com.gracefl.propfirm.repository.TradeChallengeRepository;

@Component
public class ResetDailyDrawdownService implements Runnable, Closeable {
	
	@Autowired
	TradeChallengeRepository tradeChallengeRepository;
	
	@Autowired
	Mt4AccountRepository mt4AccountRepository;
	
	private final static Logger log = LoggerFactory.getLogger(ResetDailyDrawdownService.class);
	
	private volatile boolean running = true;
    
    @Override 
    public void run() {
    	// prep the scanner
    	long start = System.nanoTime();
    	
    	TradeChallenge tradeChallengeExample = new TradeChallenge();
    	tradeChallengeExample.setRulesViolated(false);
		
        List<TradeChallenge> allChallenges = tradeChallengeRepository.findAll(Example.of(tradeChallengeExample));
        
        for (TradeChallenge challenge : allChallenges) {
        	
        	challenge.setRunningMaxDailyDrawdown(0D);
        	tradeChallengeRepository.save(challenge);
        	
        	Mt4Account account = challenge.getMt4Account();
        	account.setAccountInfoDouble(account.getAccountBalance());
        	account.setAccountInfoString(Instant.now().toString());
        	mt4AccountRepository.save(account);
        	
        }
        
        
        long elapsedTime = System.nanoTime() - start;
        log.debug("Reset daily drawdown job completed in " + elapsedTime);

    }

    public void close() throws IOException {
        running = false;
    }

}