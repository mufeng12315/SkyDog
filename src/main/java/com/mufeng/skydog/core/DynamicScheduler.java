package com.mufeng.skydog.core;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 任务调度器
 */
@Component
public class DynamicScheduler{
    private static Integer MAX_POOL_SIZE = 10;
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private Map<String,ScheduledFuture<?>> futureMap;

    @Resource
    private SdRuleCheckCoreService sdRuleCheckCoreService;

    public DynamicScheduler(){
        //创建线程池
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(MAX_POOL_SIZE);
        threadPoolTaskScheduler.initialize();
        futureMap = new HashMap<String,ScheduledFuture<?>>();
    }

    /**
     * 新增任务
     * @param ruleCheckTask
     * @param scheduleExpress
     */
    public void addTask(RuleCheckTask ruleCheckTask,String scheduleExpress){
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(ruleCheckTask,new CronTrigger(scheduleExpress) );
        futureMap.put(ruleCheckTask.getTaskCode(),future);
    }

    /**
     * 停止定时任务
     * @return
     */
    public void stopTask(String taskCode){
        ScheduledFuture<?> future = futureMap.get(taskCode);
        if(future!=null){
            future.cancel(true);
        }
    }
}
