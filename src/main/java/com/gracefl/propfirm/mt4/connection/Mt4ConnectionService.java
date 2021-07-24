package com.gracefl.propfirm.mt4.connection;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * REST controller for managing pull request to the ZeroMQ server on MT4
 */
@Service
@Transactional
public class Mt4ConnectionService {

    private final Logger log = LoggerFactory.getLogger(Mt4ConnectionService.class);

    // business logic for MT4 connections goes in here
    // generated without using an interface
    
    @Autowired
    Mt4TerminalZeroMqConnector mt4TerminalZeroMqConnector;
    
    public Optional<AccountInformation> getAccountDetails(Long accountId) throws JsonMappingException, JsonProcessingException {
    	Optional<AccountInformation> mt4Account = mt4TerminalZeroMqConnector.getAccountInformation(accountId);
    	return mt4Account;
    }


    public Optional<String> getHeartBeat() throws JsonMappingException, JsonProcessingException {
    	Optional<String> heartBeatResponse = mt4TerminalZeroMqConnector.getHeartBeat();
    	return heartBeatResponse;
    }
    
}
