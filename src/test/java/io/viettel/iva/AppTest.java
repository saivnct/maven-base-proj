package io.viettel.iva;

import io.viettel.common.threadpool.ThreadPoolFactory;
import io.viettel.config.AppfileConfig;
import io.viettel.config.SpringConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by giangbb on 03/04/2023
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class}, loader= AnnotationConfigContextLoader.class)
@ActiveProfiles("default")
public class AppTest {
    private static final Logger logger = LogManager.getLogger(AppTest.class);

    @Autowired
    private AppfileConfig appfileConfig;

    @Autowired
    private ThreadPoolFactory threadPoolFactory;

    @Test
    public void test1(){
        assertTrue(1 == 1);
    }


    @Test
    public void testCompletableFutureComplete(){
        final CompletableFuture<Long> completableFuture = new CompletableFuture<>();
        logger.debug("testCompletableFutureComplete - running on {}", Thread.currentThread().getId());
        threadPoolFactory.getCommonPools().fpRunAsync(() -> {
            logger.debug("testCompletableFutureComplete async - running on {}", Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(3);
//                throw new Exception("NO NONO NONO");
                completableFuture.complete(1000l);
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        });

        long val = -1;
        try {
            val = completableFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("testCompletableFutureComplete - running on {}", Thread.currentThread().getId());

        getFixedExecutorInfo();
        getScheduledExecutorInfo();

        assertTrue(val == 1000l);
    }


    public void getFixedExecutorInfo(){
        if(threadPoolFactory.getCommonPools().getFixedPoolExecutor() instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)threadPoolFactory.getCommonPools().getFixedPoolExecutor();
            logger.debug("[Test] - FIXED POOL INFO --------------------------------------------------------------------------");
            logger.debug("[Test] - active thread in thread pool:"+threadPoolExecutor.getActiveCount()); //Returns the approximate number of threads that are actively executing tasks.
            logger.debug("[Test] - pool size in thread pool:"+threadPoolExecutor.getPoolSize());    //Returns the current number of threads in the pool.
            logger.debug("[Test] - core pool size in thread pool:"+threadPoolExecutor.getCorePoolSize());   //Returns the core number of threads.
            logger.debug("[Test] - max pool size in thread pool:"+threadPoolExecutor.getMaximumPoolSize()); //Returns the maximum allowed number of threads.
            logger.debug("[Test] - task count in thread pool:"+threadPoolExecutor.getTaskCount());  //Returns the approximate total number of tasks that have ever been scheduled for execution
        }
    }

    public void getScheduledExecutorInfo(){
        if(threadPoolFactory.getCommonPools().getScheduledPoolExecutor() instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)threadPoolFactory.getCommonPools().getScheduledPoolExecutor();
            logger.debug("[Test] - SCHEDULED POOL INFO --------------------------------------------------------------------------");
            logger.debug("[Test] - active thread in thread pool:"+threadPoolExecutor.getActiveCount()); //Returns the approximate number of threads that are actively executing tasks.
            logger.debug("[Test] - pool size in thread pool:"+threadPoolExecutor.getPoolSize());    //Returns the current number of threads in the pool.
            logger.debug("[Test] - core pool size in thread pool:"+threadPoolExecutor.getCorePoolSize());   //Returns the core number of threads.
            logger.debug("[Test] - max pool size in thread pool:"+threadPoolExecutor.getMaximumPoolSize()); //Returns the maximum allowed number of threads.
            logger.debug("[Test] - task count in thread pool:"+threadPoolExecutor.getTaskCount());  //Returns the approximate total number of tasks that have ever been scheduled for execution
        }
    }


    //region testFixedThreadPool
    @Test
    public void testFixedThreadPool(){
        logger.debug("[Test] - running Test");

        CompletableFuture<Void> myTask = threadPoolFactory.getCommonPools().fpRunAsync(this::myTask);
        try {
            myTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void myTask(){
        logger.debug("[Test][myTask] - inside my task");

        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        for (int i = 0 ; i < 100 ; i++){
            final int taskId = i;
            CompletableFuture<Void> subTask = threadPoolFactory.getCommonPools().fpRunAsync(() -> {
                mySubTask(taskId);
            });

            tasks.add(subTask);
        }

        CompletableFuture<Void>[] tasksArr = tasks.stream().toArray(CompletableFuture[] ::new);

        CompletableFuture<Void> combinedFuture
                = CompletableFuture.allOf(tasksArr);

        try {
            combinedFuture.get();
            for (CompletableFuture<Void> task : tasks){
                logger.debug(task.isDone());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void mySubTask(int i)  {
        logger.debug("[Test][mySubTask] - inside my SubTask" + i);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    //endregion

    //region testFixedThreadPool
    @Test
    public void testScheduledThreadPool(){
        logger.debug("[Test] - running Test");

        for (int i = 0; i < 10; i++){
            final int threadId = i;
            threadPoolFactory.getCommonPools().spScheduleWithFixedDelay(() -> {
                logger.debug("Run Schedule Task: {}", threadId);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }, 100, 1000, TimeUnit.MILLISECONDS);
        }
    }
    //endregion
}
