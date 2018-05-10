/*
 *
 *      DBFactory.java
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
import com.anyun.cloud.tools.config.ConfigFactory;
import com.anyun.cloud.tools.execption.DatabaseOrmException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * MySQL数据库工厂，包含了常用MySQL操作方法
 *
 * @date 2015/1/9
 * @author TwitchGG
 * @version 1.0
 */
public class DBFactory {

    public static final String CFG_NAME_DB = "DB_CONFIG";
    public static final String JDBC_MYSQL = "com.mysql.jdbc.Driver";

    private static DBFactory factory;
    private final BasicDataSource dataSource;

    private DBFactory() {
        DBConfig dBConfig = (DBConfig) ConfigFactory.getFactory().getConfig(CFG_NAME_DB);
        int timeout = dBConfig.getTimeout();
        dataSource = new BasicDataSource();
        dataSource.setInitialSize(10);
        dataSource.setMaxIdle(20);
        dataSource.setMinIdle(5);
        dataSource.setMaxTotal(100);
        dataSource.setRemoveAbandonedOnBorrow(true);
        dataSource.setRemoveAbandonedOnMaintenance(true);
        dataSource.setRemoveAbandonedTimeout(180);
        dataSource.setMaxWaitMillis(1000);
        dataSource.setDriverClassName(dBConfig.getDriverName());
        dataSource.setUsername(dBConfig.getUserName());
        dataSource.setPassword(dBConfig.getUserPasswd());
        dataSource.setUrl(dBConfig.getUrl());
        dataSource.setValidationQuery(dBConfig.getValidationQuery());
        dataSource.setValidationQueryTimeout(timeout);
    }

    /**
     * 数据库记录更新
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public boolean update(String sql) throws Exception {
        System.out.println(sql);
        Connection connection;
        connection = getConnection();
        PreparedStatement statement;
        statement = connection.prepareStatement(sql);
        boolean flag = statement.execute();
        statement.close();
        connection.close();
        return flag;
    }

    public boolean insertEntity(BaseEntity entity) throws Exception {
        String tableName = entity.getClass().getName();
        DBTableName dbTableName = entity.getClass().getAnnotation(DBTableName.class);
        if (dbTableName != null) {
            tableName = dbTableName.value();
        }
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName);
        StringBuilder fieldBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();
        sql.append(" (");
        for (Field field : entity.getClass().getDeclaredFields()) {
            String fieldName = field.getName();
            DBField dbField = field.getAnnotation(DBField.class);
            if (dbField == null) {
                continue;
            } else {
                if (StringUtils.isNotEmpty(dbField.value())) {
                    fieldName = dbField.value();
                }
            }
            Object filedValue = OrmUtil.invokeGetFieldValue(entity, field);
            fieldBuilder.append(",").append(fieldName);
            valuesBuilder.append(",").append(OrmUtil.getFieldSql(filedValue));
        }
        sql.append(fieldBuilder.toString().substring(1)).append(" )");
        sql.append(" VALUES (").append(valuesBuilder.toString().substring(1)).append(" )");
        return update(sql.toString());
    }

    /**
     * 数据库记录查询
     *
     * @param <T>
     * @param sqlWhere
     * @param entityType
     * @return
     * @throws DatabaseOrmException
     */
    public <T extends BaseEntity> List<T> select(String sqlWhere, Class<T> entityType) throws DatabaseOrmException {
        try {
            DBTableName dbTableName = entityType.getAnnotation(DBTableName.class);
            if(dbTableName == null)
                throw new DatabaseOrmException("不是一个有效的Database ORM试题类型");
            String tableName = dbTableName.value();
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT * FROM ").append(tableName);
            if(StringUtils.isNotEmpty(sqlWhere))
                sqlBuilder.append(" WHERE ").append(sqlWhere);
            List<T> entities = new ArrayList<>();
            ResultSet resultSet = select(sqlBuilder.toString());
            while (resultSet.next()) {
                T instance = entityType.newInstance();
                for (Field field : entityType.getDeclaredFields()) {
                    DBField dbField = field.getAnnotation(DBField.class);
                    if(dbField == null)
                        continue;
                    String fieldName = field.getName();
                    if (StringUtils.isNotEmpty(dbField.value())) {
                        fieldName = dbField.value();
                    }
                    Class<?> fieldType = field.getType();
                    if(fieldType.getName().equals(java.util.Date.class.getName()))
                        fieldType = java.sql.Date.class;
                    Object dbValue = resultSet.getObject(fieldName, fieldType);
                    OrmUtil.setFieldValue(instance,field, dbValue);
                }
                entities.add(instance);
            }
            return entities;
        } catch (SQLException | InstantiationException | IllegalAccessException | SecurityException e) {
            throw new DatabaseOrmException(e.getMessage());
        }
    }
    
    public <T extends BaseEntity> T selectOne(String sqlWhere, Class<T> entityType) throws DatabaseOrmException {
        List<T> entities = select(sqlWhere, entityType);
        if(entities.isEmpty())
            return null;
        return entities.get(0);
    }

    public ResultSet select(String sql) throws SQLException {
        System.out.println(sql);
        Connection connection;
        connection = getConnection();
        PreparedStatement statement;
        statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    /**
     * 获取数据库连接
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        return null;
    }

    /**
     * 获取数据库操作工厂
     *
     * @return
     */
    public static DBFactory getFactory() {
        synchronized (DBFactory.class) {
            if (factory == null) {
                factory = new DBFactory();
            }
        }
        return factory;
    }
}
