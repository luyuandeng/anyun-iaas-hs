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

package com.anyun.esb.component.registry;

import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.common.jbi.component.BusinessService;
import org.apache.camel.spi.PackageScanClassResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/14/16
 */
public class ServiceResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceResource.class);
    public static List<BusinessService> services = new ArrayList<>();

    public static List<Class<? extends BusinessService>> getComponentServiceClasses(
            PackageScanClassResolver scanClassResolver) throws Exception {
        String[] packageNames = new String[]{
                "com.anyun.esb.component.registry.service.publish"
        };
        LOGGER.debug("Scan component services from package [{}]", packageNames);
        Set<Class<?>> classes = scanClassResolver.findImplementations(BusinessService.class, packageNames);
        List<Class<? extends BusinessService>> classList = new ArrayList<>();
        for (Class<?> aClass : classes) {
            if (aClass.getName().equals(ServiceResource.class.getName()))
                continue;
            if (aClass.getName().equals(BusinessService.class.getName()))
                continue;
            if (aClass.getName().equals(AbstractBusinessService.class.getName()))
                continue;
            LOGGER.debug("Host component service class [{}]", aClass.getName());
            classList.add((Class<? extends BusinessService>) aClass);
        }
        LOGGER.debug("Scaned class size [{}]", classList.size());
        return classList;
    }

    public static BusinessService newService(Class<? extends BusinessService> serviceClass) throws Exception {
        BusinessService anyunCloudApiService;
        anyunCloudApiService = serviceClass.newInstance();
        return anyunCloudApiService;
    }
}
