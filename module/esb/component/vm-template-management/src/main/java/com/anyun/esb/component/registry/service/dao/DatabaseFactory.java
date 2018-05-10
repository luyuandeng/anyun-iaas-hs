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

package com.anyun.esb.component.registry.service.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.PreparedStatement;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/17/16
 */
public class DatabaseFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseFactory.class);
    private static DatabaseFactory factory;
    private SqlSessionFactory sqlSessionFactory;
    private boolean error = true;

    private DatabaseFactory() {
        try {
            Resources.setDefaultClassLoader(DatabaseFactory.class.getClassLoader());
            String resource = "config/mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            LOGGER.error("Database initial error :{}", e.getMessage());
            error = true;
        }
    }

    public static DatabaseFactory getFactory() {
        synchronized (DatabaseFactory.class) {
            if (factory == null) {
                factory = new DatabaseFactory();
            }
        }
        return factory;
    }

    public boolean test() throws Exception {
        try {
            SqlSession session = sqlSessionFactory.openSession();
            String sql = "select 1 = 1 as test";
            PreparedStatement result = session.getConnection().prepareStatement(sql);
            if (result == null) {
                LOGGER.error("Database testing error.");
                throw new Exception("Database testing error.");
            }
            LOGGER.info("Database query test successed.");
            error = false;
            return true;
        } catch (Exception e) {
            LOGGER.error("Database testing error:{}", e.getMessage());
            error = true;
            return false;
        }
    }

    public SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public boolean isError() {
        return error;
    }
}
