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

package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/18/16
 */
@Path("/account/user")
public class UserApi {

    @POST
    @Path("/login/bypasswd")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = false,
            operate = "user.login.bypasswd",
            service = "user_login_by_passwd")
    public String loginByPasswd(String loginInfo) {
        return null;
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     * @see com.anyun.cloud.api.account.User
     */
    @PUT
    @Path("/add/")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            operate = "user.create",
            service = "account_user_create")
    public String add(String user) {
        return null;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("/delete/${id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            operate = "user.delete",
            service = "account_user_delete")
    public String delete(String id) {
        return null;
    }

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    @GET
    @Path("/info/{id}/")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            operate = "user.info.get",
            service = "account_user_info")
    public String getInfomartaion(@PathParam("userId") String id) {
        return null;
    }
}
