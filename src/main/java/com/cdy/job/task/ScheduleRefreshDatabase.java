package com.cdy.job.task;

import com.cdy.job.dao.ScheduleTaskRepository;
import com.cdy.job.entity.ScheduleTask;
import com.cdy.job.util.ScheduleUtil;
import com.cdy.job.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 刷新任务的任务
 * Created by 陈东一
 * 2018/10/14 0014 17:30
 */
@Component
@Slf4j
public class ScheduleRefreshDatabase {
    
    // 同步一次，然后通过事件推送来更新相应的属性
//    @Scheduled(fixedRate = 1000*60*10)
    public void scheduleUpdateCronTrigger() throws SchedulerException {
        Scheduler scheduler = (Scheduler) SpringUtil.getBean("scheduler");
        ScheduleTaskRepository taskRepository = SpringUtil.getBean(ScheduleTaskRepository.class);
        ScheduleUtil scheduleUtil = SpringUtil.getBean(ScheduleUtil.class);
        List<ScheduleTask> all = taskRepository.findAll();
        for (ScheduleTask scheduleTask : all) {
            TriggerKey triggerKey = new TriggerKey(scheduleTask.getName(), scheduleTask.getGroup());
            CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            log.info("添加 or 修改 " + (oldTrigger==null));
            if (oldTrigger == null) {
                scheduleUtil.startJob(scheduleTask.getName(),
                        scheduleTask.getGroup(),
                        scheduleTask.getCron(),
                        scheduleTask.getType(),
                        scheduleTask.getType() == 1 ? scheduleTask.getTaskUrl() : scheduleTask.getClassName());
            } else {
                //定时表达式不同 或者 是要修改远程任务的url
                //本地任务只能修改表达式，不能通过修改类路径来修改任务
                if (!oldTrigger.getCronExpression().equalsIgnoreCase(scheduleTask.getCron()) ||
                        (scheduleTask.getType() == 1 &&
                                !StringUtils.endsWithIgnoreCase((String) oldTrigger.getJobDataMap().get("url"), scheduleTask.getTaskUrl()))) {
                    
                    scheduleUtil.modifyJobTime(scheduleTask.getName(),
                            scheduleTask.getGroup(),
                            scheduleTask.getCron(),
                            scheduleTask.getType(),
                            scheduleTask.getType() == 1 ? scheduleTask.getTaskUrl() : scheduleTask.getClassName());
                }
            }
            
        }
    }
}


