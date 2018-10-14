package com.cdy.job.task;

import com.cdy.job.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.client.RestTemplate;

/**
 * 远程任务
 * Created by 陈东一
 * 2018/10/14 0014 16:54
 */
@Slf4j
public class RemoteJob implements Job {
    
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String url = jobExecutionContext.getMergedJobDataMap().getString("url");
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        log.info(url);
        if(StringUtils.isNotBlank(url))
            restTemplate.getForObject(url, String.class);
        
    }
}
