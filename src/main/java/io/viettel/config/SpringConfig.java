package io.viettel.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author : Giangbb
 * @since : 09/04/2020, Thu
 **/
@Configuration
@ComponentScan("io.viettel")
public class SpringConfig {
	private static final Logger logger = LogManager.getLogger(SpringConfig.class);

	@Autowired
	private AppfileConfig appfileConfig;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Autowired
	@Qualifier("serverKey")
	public String serverKey;

	@Bean(name = "serverKey")
	public String getServerKey(){
		String hostname = "";
		String hostAdress = "";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
			hostAdress = appfileConfig.serverAddress;
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
			logger.error("Error description:",ex);
		}
		return  hostname + "_" + hostAdress + "_" + appfileConfig.controlServerPort;
	}
	
}
