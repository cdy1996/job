package com.cdy.job.util;

import com.cdy.job.task.RemoteJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务的工具类
 * Created by 陈东一
 * 2018/10/14 0014 16:36
 */
@Component
@Slf4j
public class ScheduleUtil {
    
    @Autowired
    private Scheduler scheduler;
    
    
    /**
     * @param name  任务id
     * @param group 任务名称
     * @param time  任务时间
     * @param type  任务类别
     * @param job   任务地址或者任务指定类
     */
    public void startJob(String name, String group, String time, int type, String job) {
        log.info("添加任务" + name +"|"+ group +"|"+ time +"|"+ type +"|"+ job);
        try {
            JobDetail jobDetail = null;
            if (type == 0) {
                // 创建jobDetail实例，绑定Job实现类
                // 指明job的名称，所在组的名称，以及绑定job类
                Class jobClass = Class.forName(job);
                jobDetail = JobBuilder.newJob(jobClass)
                        .withIdentity(name, group).build();
            } else if (type == 1) {
                jobDetail = JobBuilder.newJob(RemoteJob.class)
//                        .usingJobData("url", job)
                        .withIdentity(name, group).build();
                
                jobDetail.getJobDataMap().put("url", job);//动态添加数据
            }
            
            //  corn表达式  每2秒执行一次
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time/*"0/2 * * * * ?"*/);
            //设置定时任务的时间触发规则
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(name, group)
                    .withSchedule(scheduleBuilder).build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, cronTrigger);
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        
    }
    
    /**
     * 修改定时任务时间
     *
     * @param name
     * @param group
     * @param time
     */
    public void modifyJobTime(String name, String group, String time, Integer type, String job) {
        log.info("修改任务" + name +"|"+ group +"|"+ time +"|"+ type +"|"+ job);
        try {
            TriggerKey triggerKey = new TriggerKey(name, group);
            CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (oldTrigger == null) {
                return;
            }
            log.debug(String.valueOf(scheduler.getTriggerState(triggerKey)));
            // 设置一个新的定时时间
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            // 按新的cronExpression表达式重新构建trigger
            CronTrigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder).build();
            //如果是远程任务需要把url放回去
            oldTrigger.getJobDataMap().forEach((k, v) -> newTrigger.getJobDataMap().put(k, v));
            if (type == 1) {
                
                newTrigger.getJobDataMap().put("url", job);
            }
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, newTrigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 暂停一个任务
     *
     * @param name
     * @param group
     */
    public void pauseJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return;
            }
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * 删除一个任务
     *
     * @param name
     * @param group
     */
    public void deleteJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return;
            }
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * 恢复一个任务
     *
     * @param name
     * @param group
     */
    public void resumeJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return;
            }
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * 开始定时任务
     */
    public void startAllJob() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * 立即执行定时任务
     *
     * @param name
     * @param group
     */
    public void doJob(String name, String group) {
        try {
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * 停止
     */
    public void shutdown() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
}

