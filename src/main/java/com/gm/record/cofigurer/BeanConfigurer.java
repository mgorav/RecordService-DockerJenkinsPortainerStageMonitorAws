package com.gm.record.cofigurer;

import com.gm.record.servicelocator.RecordServiceBeanLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurer {

    @Bean()
    public RecordServiceBeanLocator recordServiceBeanLocator() {
        return new RecordServiceBeanLocator();
    }
}
