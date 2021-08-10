package com.gracefl.propfirm.helpers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DatesHelper {
	
	private final Logger log = LoggerFactory.getLogger(DatesHelper.class);
	
	// @Deprecated
	public ZonedDateTime convertEpochDateTimeToZonedDateTime(Long convertEpochTime) {
		
		LocalDateTime dateTime = LocalDateTime.ofEpochSecond(convertEpochTime, 0, ZoneOffset.ofHours(0));
		//ZoneId zoneId = ZoneId.of( "Europe/Tirane" );        //Zone information
		ZoneId zoneId = ZoneId.of( "GMT" );        //Zone information
		ZonedDateTime zonedDateTime = dateTime.atZone( zoneId ); 
		
		return zonedDateTime; 
	}
	
	public ZonedDateTime convertStringToZonedDateTime(String stringDateTime) {
		// 2020.12.22 02:15
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(stringDateTime, format);
		ZoneId zoneId = ZoneId.of( "GMT" );        //Zone information
		ZonedDateTime zonedDateTime = dateTime.atZone( zoneId ); 
		
		return zonedDateTime; 
	}
	
	public Instant convertStringToInstant(String stringDateTime) {
		// 2020.12.22 02:15
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(stringDateTime, format);
		ZoneId zoneId = ZoneId.of( "GMT" );        //Zone information
		ZonedDateTime zonedDateTime = dateTime.atZone( zoneId ); 
		
		return zonedDateTime.toInstant(); 
	}
	
	// stock prices from EOD Historical have NY close 
	public ZonedDateTime convertStringToZonedDateTimeHyphens(String stringDateTime) {
		// 2019-01-02
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dateTime = LocalDate.parse(stringDateTime, format);
		ZoneId zoneId = ZoneId.of( "America/New_York" );        //Zone information
		ZonedDateTime zonedDateTime = dateTime.atStartOfDay(zoneId);
		
		return zonedDateTime; 
	}
	
}
