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
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 5/9/16
 */
@Path("/storage")
public class StorageApi {
    /**
     * 1、查询存储详情
     *
     * @param id            存储Id
     * @param userUniqueId;
     * @return List<StorageDto>
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "storage.query.by.id",
            service = "storage_query_by_id")
    public String getStorageDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 2、查询所有存储列表
     *
     * @param userUniqueId
     * @return List<StorageDto>
     */
    @GET
    @Path("/allList")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "storage.all.query",
            service = "storage_all_query")
    public String getAllStorageList(String userUniqueId) {
        return null;
    }

    /**
     * 3、创建存储
     *
     * @param body
     * @return Status<String>
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "storage.create",
            service = "storage_create")
    public String createStorage(String body) {
        return null;
    }


    /**
     * 4、修改存储
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "storage.update",
            service = "storage_update")
    public String updateStorage(String body) {
        return null;
    }


    /**
     * 5、 更改存储状态--(新添接口)
     *
     * @param body
     * @return
     */
    @POST
    @Path("/modifyState")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "storage.update.state",
            service = "storage_update_state")
    public String modifyState(String body) {
        return null;
    }

    /**
     * 6、 删除存储--(新添接口)
     *
     * @param id
     * @param userUniqueId
     * @return
     */
    @DELETE
    @Path("/deleteStorage/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "storage.delete",
            service = "storage_delete")
    public String deleteStorage(@PathParam("id") String id, String userUniqueId) {
        return null;
    }
}


