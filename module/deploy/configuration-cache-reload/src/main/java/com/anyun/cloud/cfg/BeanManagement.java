/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.anyun.cloud.cfg;

import com.anyun.cloud.cfg.db.SystemConfigDao;
import com.anyun.cloud.cfg.db.SystemConfigDaoImpl;
import com.anyun.cloud.cfg.impl.SystemPropertiesConfigurationImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/12/16
 */
public class BeanManagement {
    private static BeanManagement management;
    private Map<Class<?>, Object> beanInstances;

    private BeanManagement() {
        beanInstances = new HashMap<>();
        initBeans();
    }

    private void initBeans() {
        addBean(SystemConfigDao.class,new SystemConfigDaoImpl());
        addBean(SystemPropertiesConfiguration.class, new SystemPropertiesConfigurationImpl());
    }

    public static BeanManagement getManagement() {
        synchronized (BeanManagement.class) {
            if (management == null)
                management = new BeanManagement();
        }
        return management;
    }

    public void addBean(Class<?> type, Object instance) {
        beanInstances.put(type, instance);
    }

    public <T> T getBean(Class<T> type) {
        Object instance = beanInstances.get(type);
        if (instance == null)
            return null;
        return (T) instance;
    }
}
