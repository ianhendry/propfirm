package com.gracefl.propfirm.config;

import java.time.LocalTime;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Propfirm.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	
public final ForexMarketData forexMarketData = new ForexMarketData();
	
	public ForexMarketData getforexMarketData() {
        return forexMarketData;
    }

	public static class ForexMarketData {
		private Boolean activate;
		private String server;
		private String protocol;
		private int pushSocket;
		private int pullSocket;
		private int pubSocket;
		public String getServer() {
			return server;
		}
		
		public Boolean getActivate() {
			return activate;
		}

		public void setActivate(Boolean activate) {
			this.activate = activate;
		}

		public void setServer(String server) {
			this.server = server;
		}

		public String getProtocol() {
			return protocol;
		}
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}
		public int getPushSocket() {
			return pushSocket;
		}
		public void setPushSocket(int pushSocket) {
			this.pushSocket = pushSocket;
		}
		public int getPullSocket() {
			return pullSocket;
		}
		public void setPullSocket(int pullSocket) {
			this.pullSocket = pullSocket;
		}
		public int getPubSocket() {
			return pubSocket;
		}
		public void setPubSocket(int pubSocket) {
			this.pubSocket = pubSocket;
		}
		
	}
	
}
