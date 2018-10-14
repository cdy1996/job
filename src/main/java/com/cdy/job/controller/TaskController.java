package com.cdy.job.controller;

import com.cdy.job.entity.ScheduleTask;
import com.cdy.job.util.ScheduleUtil;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务控制层
 * Created by 陈东一
 * 2018/10/14 0014 17:57
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    ScheduleUtil scheduleUtil;
    @Autowired
    Scheduler scheduler;
    
    @PostMapping
    @ResponseBody
    public String addTask(ScheduleTask task){
        scheduleUtil.startJob(task.getName(), task.getGroup(), task.getCron(), task.getType(), task.getType()==1?task.getTaskUrl():task.getClassName());
        return "add ok";
    }
    
    @PutMapping
    @ResponseBody
    public String updateTask(ScheduleTask task){
        scheduleUtil.modifyJobTime(task.getName(), task.getGroup(), task.getCron(), task.getType(), task.getType()==1?task.getTaskUrl():task.getClassName());
        return "update ok";
    }
    
    @GetMapping("/pause")
    @ResponseBody
    public String pauseTask(ScheduleTask task){
        scheduleUtil.pauseJob(task.getName(), task.getGroup());
        return "pause ok";
    }
    
    @GetMapping("/resume")
    @ResponseBody
    public String resumeTask(ScheduleTask task){
        scheduleUtil.resumeJob(task.getName(), task.getGroup());
        return "resume ok";
    }
    
    @GetMapping("/all")
    @ResponseBody
    public List<String> listTask() throws SchedulerException {
        return scheduler.getTriggerGroupNames();
    }
    
    

}
