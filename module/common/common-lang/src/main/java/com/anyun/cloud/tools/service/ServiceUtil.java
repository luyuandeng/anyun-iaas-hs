/*
 *
 *      ServiceUtil.java
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
package com.anyun.cloud.tools.service;

import com.anyun.cloud.tools.db.BaseEntity;
import com.anyun.cloud.tools.db.DBFactory;
import com.anyun.cloud.tools.execption.BaseServiceException;
import com.anyun.cloud.tools.execption.DatabaseOrmException;
import java.util.List;

/**
 *
 * @author TwitchGG
 * @date 2015-4-27
 * @version 1.0
 */
public class ServiceUtil {

    public static void insertEntity(BaseEntity entity) throws BaseServiceException {
        try {
            DBFactory.getFactory().insertEntity(entity);
        } catch (Exception ex) {
            throw new BaseServiceException(500, "数据库异常");
        }
    }

    public static <T extends BaseEntity> List<T> selectEnties(Class<T> entityType) throws BaseServiceException {
        List<T> entities = null;
        try {
            entities = DBFactory.getFactory().select(null, entityType);
        } catch (Exception e) {
            throw new BaseServiceException(500, "数据库异常");
        }
        return entities;
    }

    public static <T extends BaseEntity> List<T> selectEnties(Class<T> entityType, StringBuilder conditions) throws BaseServiceException {
        DBFactory dbFactory = DBFactory.getFactory();
        List<T> entities = null;
        try {
            entities = dbFactory.select(conditions.toString(), entityType);
        } catch (DatabaseOrmException e) {
            throw new BaseServiceException(500, "数据库异常");
        }
        return entities;
    }
}
