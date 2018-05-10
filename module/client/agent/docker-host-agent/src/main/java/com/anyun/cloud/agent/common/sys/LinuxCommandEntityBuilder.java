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

package com.anyun.cloud.agent.common.sys;

import com.anyun.cloud.tools.bash.BashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/17/16
 */
public class LinuxCommandEntityBuilder<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxCommandEntityBuilder.class);

    public LinuxCommandEntityBuilder() {

    }

    public T build(T entity) {
        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                BaseLinuxCommandEntity.Command commandAnnotation = field.getAnnotation(BaseLinuxCommandEntity.Command.class);
                if (commandAnnotation == null) {
                    continue;
                }
                String command = commandAnnotation.value();
                try {
                    BashCommand bashCommand = new BashCommand(command);
                    Class<?> process = commandAnnotation.process();
                    String baseResult = bashCommand.exec().trim();
                    Object result = null;
                    if (process.equals(NullProcess.class)) {
                        result = baseResult;
                        result = String.valueOf(result);
                    } else {
                        try {
                            BaseProcess bp = (BaseProcess) process.newInstance();
                            result = bp.process(baseResult, commandAnnotation.args());
                        } catch (Exception e) {
                            LOGGER.error("Exception: ", e.getMessage(), e);
                        }
                    }
                    setFieldValue(entity, field, result);
                } catch (Exception e) {
                }
            }
            return entity;
        } catch (Exception e) {
            return null;
        }
    }

    private void setFieldValue(Object entity, Field field, Object value) {
        Class<?> type = field.getType();
        String fieldName = field.getName();
        String methodName = "set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
        try {
            Method method = entity.getClass().getMethod(methodName, type);
            if (type.equals(Integer.class) || type.getName().equals("int")) {
                method.invoke(entity, Integer.valueOf(String.valueOf(value)));
            } else if (type.equals(Long.class) || type.getName().equals("long")) {
                method.invoke(entity, Long.valueOf(String.valueOf(value)));
            } else if (type.equals(String.class)) {
                method.invoke(entity, (String) value);
            } else {
                method.invoke(entity, value);
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        }
    }
}
