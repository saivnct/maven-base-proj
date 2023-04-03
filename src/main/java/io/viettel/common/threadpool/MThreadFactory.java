package io.viettel.common.threadpool;

import java.util.concurrent.ThreadFactory;

/**
 * Created by giangbb on 07/03/2023
 */
public class MThreadFactory implements ThreadFactory {
    private final String threadPrefix;
    private final String poolName;
    private int threadCount = 0;

    public MThreadFactory(String threadPrefix, String poolName) {
        this.threadPrefix = threadPrefix;
        this.poolName = poolName;
    }

    public Thread newThread(Runnable r) {
        String threadName = threadPrefix + " {pool:" + poolName
                + ", thread:" + (threadCount++) + "}";

//			logger.debug("new thread created:"+threadName);

        return new Thread(r, threadName);
    }
}
