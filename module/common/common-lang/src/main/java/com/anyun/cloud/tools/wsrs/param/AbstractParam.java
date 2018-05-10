/*
 *
 *      AbstractParam.java
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
package com.anyun.cloud.tools.wsrs.param;

import com.anyun.cloud.tools.db.AbstractEntity;
import com.anyun.cloud.tools.db.OrmUtil;
import com.anyun.cloud.tools.execption.ServiceParamCheckException;
import com.anyun.cloud.tools.wsrs.UserSessionManagement;

import java.lang.reflect.Field;

/**
 *
 * @author TwitchGG
 * @param <T>
 * @date 2015-4-27
 * @version 1.0
 */
public abstract class AbstractParam<T> extends AbstractEntity implements BaseServiceParam<T> {
    private String sessionTick;
    public String getSessionTick() {
        return sessionTick;
    }

    public void setSessionTick(String sessionTick) {
        this.sessionTick = sessionTick;
    }

    @Override
    public T check() throws ServiceParamCheckException {
        T param = (T) this;
        checkParam(param);
        return param;
    }
    
    
    protected final void checkParam(T param) throws ServiceParamCheckException {
        for (Field field : param.getClass().getDeclaredFields()) {
            MustCheck check = field.getAnnotation(MustCheck.class);
            if (check != null) {
                for (Class<? extends ParamChecker> checkerClass : check.checker()) {
                    ParamChecker checker;
                    Object paramValue; 
                    try {
                       paramValue = OrmUtil.invokeGetFieldValue(param, field);
                       checker = checkerClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        String errorMessage = "参数检查器异常";
                        if(e instanceof InstantiationException)
                            errorMessage = "参数检查器初始化异常";
                        else if(e instanceof IllegalAccessException) {
                            errorMessage = "参数检查器获取参数值异常";
                        }
                        throw new ServiceParamCheckException(902, errorMessage);
                    }
                    checker.check(check.paramDesc(), paramValue,check.args());
                }
            }
        }
    }
}
