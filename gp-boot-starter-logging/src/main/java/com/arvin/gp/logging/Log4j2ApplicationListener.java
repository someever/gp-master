package com.arvin.gp.logging;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 应用事件监听， 加入一些自定义参数
 * <p/>
 *
 * @author arvin.
 * @date 2018/6/9 15:54.
 */
public class Log4j2ApplicationListener implements GenericApplicationListener {
    public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 20;
    private static Class<?>[] EVENT_TYPES = {ApplicationStartingEvent.class,
            ApplicationEnvironmentPreparedEvent.class, ApplicationPreparedEvent.class};
    private static Class<?>[] SOURCE_TYPES = {SpringApplication.class, ApplicationContext.class};

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ApplicationEnvironmentPreparedEvent) {
            ConfigurableEnvironment env = ((ApplicationEnvironmentPreparedEvent) applicationEvent).getEnvironment();
            MDC.put("appName", env.getProperty("spring.application.name"));
        }
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if(type != null){
            for (Class<?> supportedType : supportedTypes){
                if(supportedType.isAssignableFrom(type)){
                    return true;
                }
            }
        }
        return false;
    }
}
