/*
 *
 *      OrmUtil.java
 *      Copyright (C) <2015-?>  <twitchgg@yahoo.com>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anyun.cloud.tools.db;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.execption.DatabaseOrmException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 *
 * @author TwitchGG
 * @date 2015-4-27
 * @version 1.0
 */
public class OrmUtil {

    public static String OP_LIKE = " LIKE ";
    public static String TP_AND = " AND ";
    public static String TP_OR = " OR ";

    public static Object invokeGetFieldValue(Object entity, Field field) {
        String fieldName = field.getName();
        String methodName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
        try {
            Method method = entity.getClass().getMethod(methodName);
            return method.invoke(entity);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }

    public static void setFieldValue(Object entity, Field field, Object value) throws DatabaseOrmException {
        String fieldName = field.getName();
        Class<?> fieldType = field.getType();
        String methodName = "set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
        try {
            Method method = entity.getClass().getMethod(methodName, fieldType);
            if (fieldType.equals(Integer.class) || fieldType.getName().equals("int")) {
                method.invoke(entity, Integer.valueOf(String.valueOf(value)));
            } else if (fieldType.equals(Long.class) || fieldType.getName().equals("long")) {
                method.invoke(entity, Long.valueOf(String.valueOf(value)));
            } else if (fieldType.equals(String.class)) {
                method.invoke(entity, (String) value);
            } else if (fieldType.equals(Date.class)) {
                java.sql.Date datetime = (java.sql.Date) value;
                method.invoke(entity, new Date(datetime.getTime()));
            } else if (fieldType.equals(Boolean.class) || fieldType.getName().equals("boolean")) {
                Boolean bool = (int) value != 0;
                method.invoke(entity, bool);
            } else {
                method.invoke(entity, value);
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DatabaseOrmException(e.getMessage());
        }
    }

    public static String getFieldSql(Object value) {
        if (value instanceof Long
                || value instanceof Integer
                || value instanceof Double
                || value instanceof Float) {
            return String.valueOf(value);
        } else if (value instanceof Date) {
            return String.valueOf("'" + StringUtils.formatDate((Date) value, null) + "'");
        } else if (value instanceof Boolean || value.getClass().getName().equals("boolean")) {
            Boolean bool = (boolean) value;
            return String.valueOf(bool ? 1 : 0);
        } else {
            return String.valueOf("'" + value + "'");
        }
    }

    public static String condition(String key, String op, Object value) {
        StringBuilder whereSQL = new StringBuilder();
        if (value instanceof Long
                || value instanceof Integer
                || value instanceof Double
                || value instanceof Float) {
            return whereSQL.append(" ").append(key).append(op).append(value).append(" ").toString();
        } else {
            return whereSQL.append(" ").append(key).append(op).append("'").append(value).append("' ").toString();
        }
    }
}
