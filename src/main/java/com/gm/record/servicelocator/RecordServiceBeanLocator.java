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
package com.gm.record.servicelocator;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.Map;

public class RecordServiceBeanLocator implements BeanFactoryAware {

    private static ListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        RecordServiceBeanLocator.beanFactory = (ListableBeanFactory) beanFactory;
    }


    public static <T> T beanByType(Class<T> beanType) {
        if (beanFactory == null) {
            return null;
        }

        Map<String, T> beansMap = beanFactory.getBeansOfType(beanType);

        return beansMap.isEmpty() ? null : beansMap.values().iterator().next();
    }
}
