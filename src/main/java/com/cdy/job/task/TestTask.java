package com.cdy.job.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 测试任务
 * Created by 陈东一
 * 2018/10/14 0014 17:30
 */
@Component
@Slf4j
public class TestTask implements Job {
    
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("test task");
    }
}


