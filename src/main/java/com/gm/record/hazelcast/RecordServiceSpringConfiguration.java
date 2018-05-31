/**
 * Copyright (C) 2017, Ingenico ePayments - https://www.ingenico.com/epayments
 * <p>
 * All rights reserved
 * <p>
 * This software is owned by Ingenico ePayments (hereinafter the Owner).
 * No material from this software owned, operated and controlled by the Owner
 * may be copied, reproduced, republished, uploaded, posted, transmitted, or
 * distributed in any way by any third party without the Owner's explicit
 * written consent. All intellectual and other property rights of this software
 * are held by the Owner. No rights of any kind are licensed or assigned or
 * shall otherwise pass to third parties making use of this software. Said use
 * by third parties shall only be in accordance with applicable license
 * agreements between such party and the Owner. Making, acquiring, or using
 * unauthorized copies of this software or other copyrighted materials may
 * result in disciplinary or legal action as the circumstances may warrant.
 */
package com.gm.record.hazelcast;

import com.hazelcast.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.gm.record.hazelcast.RecordServiceHazelcastConfigBuilder.newHazelcastConfigBuilderWith;

@Configuration
@Slf4j
public class RecordServiceSpringConfiguration {

    @Autowired
    private ListableBeanFactory beanFactory;


    @Autowired
    private RecordServiceHazelcastClusterProperty clusterProperty;

    @Bean
    public Config hazelCastConfig() {


        return newHazelcastConfigBuilderWith(clusterProperty.members).withBeanFactory(beanFactory).buildHazelcastConfig();
    }
}
