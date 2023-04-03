package io.viettel.common.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Created by giangbb on 07/03/2023
 */
public class ThreadPoolService {
    private static final Logger logger = LogManager.getLogger(ThreadPoolService.class);

    private ExecutorService cp;
    private ExecutorService fp;
    private ScheduledExecutorService sp;

    public ThreadPoolService(String poolName,
                             boolean shouldCreateFixedPool, int fixedPoolThreadNum,
                             boolean shouldCreateCachedPool,
                             boolean shouldCreateScheduledPool, int scheduledPoolNum){
        String poolThreadPrefix = "thread_"+poolName;
        if (shouldCreateFixedPool){
            fp = Executors.newFixedThreadPool(fixedPoolThreadNum, new MThreadFactory(poolThreadPrefix+"-fixed", poolName+"-fixed"));
        }

        if (shouldCreateCachedPool){
            cp = Executors.newCachedThreadPool(new MThreadFactory(poolThreadPrefix+"-cached", poolName+"-cached"));
        }

        if (shouldCreateScheduledPool){
            sp = Executors.newScheduledThreadPool(scheduledPoolNum, new MThreadFactory(poolThreadPrefix+"-scheduled", poolName+"-scheduled"));
        }
    }

    //region FIXED POOL
    public ExecutorService getFixedPoolExecutor(){
        return fp;
    };
    public CompletableFuture<Void> fpRunAsync(Runnable runnable){
        if (fp == null) {
            return null;
        }
        return CompletableFuture.runAsync(runnable, fp);
    }
    public <U> CompletableFuture<U> fpSupplyAsync(Supplier<U> supplier) {
        if (fp == null) {
            return null;
        }
        return CompletableFuture.supplyAsync(supplier, fp);
    }
    //endregion


    //region CACHED POOL
    public ExecutorService getCachedPoolExecutor(){
        return cp;
    };
    public CompletableFuture<Void> cpRunAsync(Runnable runnable){
        if (cp == null) {
            return null;
        }
        return CompletableFuture.runAsync(runnable, cp);
    }
    public <U> CompletableFuture<U> cpSupplyAsync(Supplier<U> supplier) {
        if (cp == null) {
            return null;
        }
        return CompletableFuture.supplyAsync(supplier, cp);
    }
    //endregion


    //region SCHEDULED POOL
    public ScheduledExecutorService getScheduledPoolExecutor(){
        return sp;
    };
    public ScheduledFuture<?> spSchedule(Runnable runnable, long delay, TimeUnit unit){
        if (sp == null) {
            return null;
        }
        return sp.schedule(runnable, delay, unit);
    }
    public ScheduledFuture<?> spScheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit){
        if (sp == null) {
            return null;
        }
        return sp.scheduleAtFixedRate(runnable, initialDelay, period, unit);
    }
    public ScheduledFuture<?> spScheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit unit){
        if (sp == null) {
            return null;
        }
        return sp.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
    }
    //endregion

    public void shutdown(){
        if (cp != null) {
            cp.shutdown();
        }


        if (fp != null) {
            fp.shutdown();
        }

        if (sp != null) {
            sp.shutdown();
        }


        if (cp != null){
            try {
                if (!cp.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    cp.shutdownNow();
                }
            } catch (InterruptedException e) {
                cp.shutdownNow();
            }
        }

        if (fp != null) {
            try {
                if (!fp.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    fp.shutdownNow();
                }
            } catch (InterruptedException e) {
                fp.shutdownNow();
            }
        }

        if (sp != null) {
            try {
                if (!sp.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    sp.shutdownNow();
                }
            } catch (InterruptedException e) {
                sp.shutdownNow();
            }
        }
    }
}
