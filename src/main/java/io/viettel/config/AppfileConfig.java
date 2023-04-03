package io.viettel.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author : Giangbb
 * @since : 09/04/2020, Thu
 **/

// use resource : classpath:application-${spring.profiles.active:default}.properties
// use file : file:${spring.profiles.active:/opt/application.properties}
@PropertySource(value = {
        "classpath:application-${spring.profiles.active:default}.properties"
})
@Component
@Qualifier("appfileConfig")
public class AppfileConfig {
    @Value("${server.env.profile}")
    public String serverEnvProfile;

    @Value("${log4j.configuration}")
    public String log4jConfiguration;

    // ///////////////SERVER CONFIG/////////////////
    @Value("${server.address}")
    public String serverAddress;

    @Value("${server.port.control}")
    public int controlServerPort;

    // ///////////////THREAD POOL CONFIG/////////////////
    @Value("${thread.pool.prefix}")
    public String threadPoolPrefix;

    @Value("${thread.pool.fixed.num}")
    public int threadPoolFixedNum;

    @Value("${thread.pool.scheduled.num}")
    public int threadPoolScheduledNum;


    @Value("${thread.pool.pubsub.prefix}")
    public String threadPoolPubSubPrefix;

    @Value("${thread.pool.pubsub.fixed.num}")
    public int threadPoolPubSubFixedNum;

    @Value("${thread.pool.pubsub.scheduled.num}")
    public int threadPoolPubSubScheduledNum;


}
