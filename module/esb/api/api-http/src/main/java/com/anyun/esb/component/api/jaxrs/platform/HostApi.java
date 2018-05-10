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
import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 5/4/16
 */
@Path("/host")
public class HostApi {
    /**
     * 1、查询宿主机详情
     *
     * @param id
     * @retuen HostBaseInfoDto
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "hostInfo.query.by.id",
            service = "hostInfo_query_by_id"
    )
    public String getDetails(@PathParam("id") String id) {
        return null;
    }

    /**
     * 2、获取 所有宿主机列表
     *
     * @param
     * @return List<HostBaseInfoDto>
     */
    @GET
    @Path("/allList")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "hostInfo.allList.query",
            service = "hostInfo_allList_query")
    public String getAllList() {
        return null;
    }

    /**
     * 3、查询宿主机实时数据列表
     *
     * @param
     * @retuen List<HostRealTimeDto>
     */
    @GET
    @Path("/realTimeList")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "hostRealTimeInfo.query",
            service = "hostRealTimeInfo_query"
    )
    public String getRealTimeList() {
        return null;
    }

    /**
     * 4、创建宿主机
     * @param body
     * @return
     */
    @PUT
    @Path("/hostCreate")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "host.create",
            service = "host_create")
    public String hostCreate(String body){return null;}

    /**
     * 5.启用或禁用宿主机（修改宿主机status）
     * @param body
     * @return
     */
    @POST
    @Path("/hostStatusUpdate")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "host.status.update",
            service = "host_status_update")
    public String hostStatusUpdate(String body){return null;}

    /**
     * 6.删除宿主机
     * @param ip
     * @return
     */
    @DELETE
    @Path("/delete/{ip}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "host.delete",
            service = "host_delete")
    public String deleteHost(@PathParam("ip") String ip,String userUniqueId){return null;}

    @POST
    @Path("/getQueueFromQpid")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "get.queue.from.qpid",
            service = "get_queue_from_qpid")
    public String getQueueFromQpid(String body){return null;}
}
