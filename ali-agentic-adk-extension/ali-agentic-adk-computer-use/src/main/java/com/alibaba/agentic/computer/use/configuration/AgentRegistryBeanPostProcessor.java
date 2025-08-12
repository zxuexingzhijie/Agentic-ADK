package com.alibaba.agentic.computer.use.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AgentRegistryBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (StringUtils.equalsIgnoreCase(beanName, "loadedAgentRegistry")) {
            return new HashMap<>();
        }
        return bean;
    }

}
