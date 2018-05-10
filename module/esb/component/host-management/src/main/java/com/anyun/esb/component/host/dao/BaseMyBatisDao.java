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

package com.anyun.esb.component.host.dao;

import com.anyun.cloud.tools.db.AbstractEntity;
import com.anyun.exception.DaoException;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/11/16
 */
public class BaseMyBatisDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMyBatisDao.class);
    protected DatabaseFactory databaseFactory;

    public BaseMyBatisDao() {
        databaseFactory = DatabaseFactory.getFactory();
    }

    public BaseMyBatisDao(DatabaseFactory databaseFactory) {
        this.databaseFactory = databaseFactory;
    }

    public DatabaseFactory getDatabaseFactory() {
        return databaseFactory;
    }

    public void setDatabaseFactory(DatabaseFactory databaseFactory) {
        this.databaseFactory = databaseFactory;
    }

    public <T> T selectOne(Class<T> type, String sql, Object params) throws DaoException {
        SqlSession session = databaseFactory.getSession();
        T dto;
        try {
            if (params == null) {
                dto = session.selectOne(sql);
            } else {
                dto = session.selectOne(sql, params);
            }
        } catch (Exception e) {
            throw new DaoException(0x00000021, e);
        } finally {
            session.close();
        }
        if (dto instanceof AbstractEntity) {
            LOGGER.debug("seletOne DTO '{}'", ((AbstractEntity) dto).asJson());
        } else {
            LOGGER.debug("seletOne DTO '{}'", dto);
        }

        return dto;
    }

    public <T> T selectById(Class<T> type, String sql, String id) {
        return selectOne(type, sql + ".selectById", id);
    }

    public <T> List<T> selectList(Class<T> type, String sql, Object params) {
        SqlSession session = databaseFactory.getSession();
        List<T> dtos = null;
        try {
            if (params == null) {
                dtos = session.selectList(sql);
            } else {
                dtos = session.selectList(sql, params);
            }
        } catch (Exception e) {
            throw new DaoException(0x00000021, e);
        } finally {
            session.close();
        }
        if (dtos != null && !dtos.isEmpty()) {
            for (T dto : dtos) {
                if (dto instanceof AbstractEntity) {
                    LOGGER.debug("seletList DTO '{}'", ((AbstractEntity) dto).asJson());
                } else {
                    LOGGER.debug("seletList DTO '{}'", dto);
                }
            }
        }
        return dtos;
    }

    public <T> List<T> selectPageList(Class<T> type, String sql, Object params, int pageNumber, int pageSize) {
        SqlSession session = databaseFactory.getSession();
        List<T> dtos = null;
        try {
            if (params == null) {
                dtos = session.selectList(sql);
            } else {
                dtos = session.selectList(sql, params, new RowBounds(pageNumber, pageSize));
            }
        } catch (Exception e) {
            throw new DaoException(0x00000021, e);
        } finally {
            session.close();
        }
        if (dtos != null && !dtos.isEmpty()) {
            for (T dto : dtos) {
                if (dto instanceof AbstractEntity) {
                    LOGGER.debug("seletList DTO '{}'", ((AbstractEntity) dto).asJson());
                } else {
                    LOGGER.debug("seletList DTO '{}'", dto);
                }
            }
        }
        return dtos;
    }

    public int insert(String sql, Object params) throws DaoException {
        try (SqlSession session = databaseFactory.getSession()) {
            int result = session.insert(sql, params);
            session.commit();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException(0x00000021, e);
        }
    }

    public SqlSession getBatchSqlSession() throws DaoException {
        try {
            SqlSession session = databaseFactory.getSqlSessionFactory().openSession(ExecutorType.BATCH);
            return session;
        } catch (Exception e) {
            throw new DaoException(0x00000021, e);
        }
    }

    public int update(String sql, Object params) throws DaoException {
        try (SqlSession session = databaseFactory.getSession()) {
            int result = session.update(sql, params);
            session.commit();
            return result;
        } catch (Exception e) {
            throw new DaoException(0x00000021, e);
        }
    }

    public int delete(String sql, Object params) throws DaoException {
        try (SqlSession session = databaseFactory.getSession()) {
            int result = session.update(sql, params);
            session.commit();
            return result;
        } catch (Exception e) {
            throw new DaoException(0x00000021, e);
        }
    }
}
