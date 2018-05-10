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

package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.dto.StorageDto;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.StorageService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 5/12/16
 */
public class TestStorageService extends BaseAnyunTest {
    private StorageService storageService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        storageService = factory.getStorageService();
        ResourceClient.setUserToken("debug_token");
    }

    //1、查询存储详情
    @Test
    public void getStorageDetails() {
        String id = "02587dcbaa8b47d7a2a7b545e90efb8f";
        String userUniqueId = "2";
        StorageDto dto = storageService.storageQueryById(id, userUniqueId);
        System.out.print(JsonUtil.toJson(dto));
    }

    //2、查询所有存储列表
    @Test
    public void getALlStorageList() {
        String userUniqueId = "";
        List<StorageDto> list = storageService.storageAllQuery(userUniqueId);
        System.out.print(list);
    }

    //3、创建存储
    @Test
    public void create() {
        StorageCreateParam param = new StorageCreateParam();
        param.setName("测试");
        param.setDescr("测试");
        param.setFilesystem("172.18.3.2:/test-volume");
        param.setPurpose("docker.volume");//存储用途 (docker.runtime , docker.volume)
        param.setType("gluster");  //gluster   nfs   yeestore
        param.setUserUniqueId("sxt");
        StorageDto dto = storageService.storageCreate(param);
        System.out.print(JsonUtil.toJson(dto));
    }


    //4、修改存储
    @Test
    public void update() {
        StorageUpdateParam param = new StorageUpdateParam();
        param.setId("02587dcbaa8b47d7a2a7b545e90efb8f");
        param.setUserUniqueId("2");
        StorageDto dto =storageService.storageUpdate(param);
        System.out.print(JsonUtil.toJson(dto));
    }

    //==================================新添接口测试========================================

    //5、更新存储状态
    @Test
    public void updateStorageState(){
        StorageUpdateStateParam param = new StorageUpdateStateParam();
        param.setId("02587dcbaa8b47d7a2a7b545e90efb8f");
        param.setAvailableState("不可用");
        param.setUserUniqueId("2");
        storageService.storageUpdateState(param);
    }

    //6、删除存储
    @Test
    public void deleteStorage(){
        String id = "08650108bf9a46229255e754109025fe";
        String userUniqueId = "2";
        storageService.storageDelete(id,userUniqueId);
    }
}
