package com.cdy.job.entity;

import lombok.Data;

import javax.persistence.*;


/**
 * 定时任务实体类
 * Created by 陈东一
 * 2018/10/14 0014 16:15
 */
@Entity
@Data
@Table(name = "schedule")
public class ScheduleTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "task_name")
    private String name;
    
    @Column(name = "group_name")
    private String group;
    
    @Column
    private String cron;
    
    @Column
    private String taskUrl;
    
    @Column
    private String className;
    
    /**
     * 本地调用为0 --className
     * 远程调用为1 --taskUrl
     */
    @Column
    private Integer type;
}
