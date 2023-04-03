package io.viettel.common.threadpool;


import io.viettel.config.AppfileConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * Created by Giangbb on 02/08/2023
 */
@Component
@Qualifier("threadPoolFactory")
public class ThreadPoolFactory {
	private static final Logger logger = LogManager.getLogger(ThreadPoolFactory.class);

	private final ThreadPoolService commonPools;
	private final ThreadPoolService pubsubPools;


	@Autowired
	public ThreadPoolFactory(AppfileConfig appfileConfig) throws Exception{
		//init Common pool
		final String COMMON_POOL_NAME = appfileConfig.threadPoolPrefix;
		final int COMMON_FIXED_THREAD_NUM = appfileConfig.threadPoolFixedNum;
		final int COMMON_SCHEDULED_THREAD_NUM = appfileConfig.threadPoolScheduledNum;
		if (StringUtils.isEmpty(COMMON_POOL_NAME) || COMMON_FIXED_THREAD_NUM <= 0 || COMMON_SCHEDULED_THREAD_NUM <= 0){
			throw new Exception("[ThreadPoolFactory] - Invalid Common Pool Params");
		}
		this.commonPools = new ThreadPoolService(COMMON_POOL_NAME,
				true, COMMON_FIXED_THREAD_NUM,
				true,
				true, COMMON_SCHEDULED_THREAD_NUM);

		//init PubSub pool
		final String PUBSUB_POOL_NAME = appfileConfig.threadPoolPubSubPrefix;
		final int PUBSUB_FIXED_THREAD_NUM = appfileConfig.threadPoolPubSubFixedNum;
		final int PUBSUB_SCHEDULED_THREAD_NUM = appfileConfig.threadPoolPubSubScheduledNum;
		if (StringUtils.isEmpty(PUBSUB_POOL_NAME) || PUBSUB_FIXED_THREAD_NUM <= 0 || PUBSUB_SCHEDULED_THREAD_NUM <= 0){
			throw new Exception("[ThreadPoolFactory] - Invalid PubSub Pool Params");
		}
		this.pubsubPools = new ThreadPoolService(PUBSUB_POOL_NAME,
				true, PUBSUB_FIXED_THREAD_NUM,
				true,
				true, PUBSUB_SCHEDULED_THREAD_NUM);

	}

	@PreDestroy
	public void shutdownThreadPool(){
		logger.info("Shutting down ThreadPoolFactory ...");
		commonPools.shutdown();
		pubsubPools.shutdown();

		logger.info("Shutdown ThreadPoolFactory!");
	}

	public ThreadPoolService getCommonPools() {
		return commonPools;
	}

	public ThreadPoolService getPubsubPools() {
		return pubsubPools;
	}
}
