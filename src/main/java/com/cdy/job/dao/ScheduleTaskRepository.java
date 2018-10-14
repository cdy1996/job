package com.cdy.job.dao;

import com.cdy.job.entity.ScheduleTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 持久层
 * Created by 陈东一
 * 2018/10/14 0014 17:32
 */
@Repository
public interface ScheduleTaskRepository extends JpaRepository<ScheduleTask, Long> {
}
