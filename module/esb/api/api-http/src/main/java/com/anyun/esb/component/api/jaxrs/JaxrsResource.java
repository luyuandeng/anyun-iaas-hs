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

package com.anyun.esb.component.api.jaxrs;

import com.anyun.cloud.tools.StringUtils;
import org.apache.camel.spi.PackageScanClassResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/18/16
 */
public class JaxrsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsResource.class);
    private static final String API_PREFIX = "com.anyun.esb.component.api.jaxrs.";
    public static List<Class<?>> getApiServices(
            PackageScanClassResolver scanClassResolver) throws Exception {
        Jedis jedis = new Jedis("redis.dev.anyuncloud.com", 6379);
        String pkgs = jedis.get("com.anyun.api.http.packages");
        String[] pkgValues = StringUtils.getSplitValues(pkgs, ",");
        List<Class<?>> classList = new ArrayList<>();
        for (String packageName : pkgValues) {
            if (StringUtils.isEmpty(packageName))
                continue;
            try {
                packageName = API_PREFIX + packageName;
                Package aPackage = Package.getPackage(packageName);
                if (aPackage == null)
                    continue;
            } catch (Exception ex) {
                continue;
            }
            LOGGER.debug("Scan anyun cloud jaxrs resource from package [{}]", packageName);
            Set<Class<?>> classes = scanClassResolver.findImplementations(Object.class, packageName);

            for (Class<?> aClass : classes) {
                try {
                    validateJaxrsClass(aClass);
                    classList.add(aClass);
                } catch (Exception ex) {
                    continue;
                }
            }
        }
        LOGGER.debug("Scaned class size [{}]", classList.size());
        return classList;
    }

    private static void validateJaxrsClass(Class<?> aClass) throws Exception {
        Path path = aClass.getAnnotation(Path.class);
        if (path == null)
            throw new Exception("class not have [Path] annotation");
        LOGGER.debug("find jaxrs api define class [{}]", aClass);
    }

    public static Annotation getHttpMethodAnnotation(Method method) throws Exception{
        Annotation httpMethod = method.getAnnotation(GET.class);
        if (httpMethod == null)
            httpMethod = method.getAnnotation(POST.class);
        if (httpMethod == null)
            httpMethod = method.getAnnotation(PUT.class);
        if (httpMethod == null)
            httpMethod = method.getAnnotation(DELETE.class);
        if(httpMethod == null)
            throw new UnsupportedOperationException("Unsupported HTTP Method");
        return httpMethod;
    }
}
