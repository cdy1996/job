package com.cdy.job.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring工具类
 * Created by 陈东一
 * 2018/10/14 0014 18:24
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    
    public static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    
    public static <T> T getBean(Class<T> t){
        return applicationContext.getBean(t);
    }
    
    public static Object getBean(String bean){
        return applicationContext.getBean(bean);
    }
}
