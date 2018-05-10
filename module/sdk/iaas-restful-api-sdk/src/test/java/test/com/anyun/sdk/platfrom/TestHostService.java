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

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.dto.HostRealTimeDto;
import com.anyun.cloud.dto.QpidQueueDto;
import com.anyun.cloud.param.HostCreateParam;
import com.anyun.cloud.param.HostStatusUpdateParam;
import com.anyun.cloud.param.QpidQueueParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.HostService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.ctc.wstx.util.StringUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 5/6/16
 */
public class TestHostService extends BaseAnyunTest {
    private HostService hostService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        hostService = factory.getHostService();
        ResourceClient.setUserToken("debug_token");
    }

    //1、查询宿主机详情
    @Test
    public void getDetails() {
        String id = "028E6CDD1B70EDA57F1BCBCEFAF125E9";
        String userUniqueId = "ds";
        HostBaseInfoDto dto = hostService.queryHostInfoById(id, userUniqueId);
        System.out.println(JsonUtil.toJson(dto));
    }


    //2、查询所有宿主机列表
    @Test
    public void getALlHostList() {
        String userUniqueId = "huangwentao";
        List<HostBaseInfoDto> l = hostService.queryAllHostInfo(userUniqueId);
        for (HostBaseInfoDto h : l) {
            System.out.println(JsonUtil.toJson(h));
            System.out.println(JsonUtil.toJson(h.getDockerHostInfoDto()));
            System.out.println(JsonUtil.toJson(h.getHostExtInfoDto()));
            System.out.println(JsonUtil.toJson(h.getHostManagementIpInfoDto()));
        }
    }

    //3、查询宿主机实时数据(for创建容器)
    @Test
    public void getRealTimeList() {
        String userUniqueId = "fd";
        List<HostRealTimeDto> dto = hostService.hostRealTimeInfoQuery(userUniqueId);
        System.out.println(JsonUtil.toJson(dto));
    }


    //创建宿主机
    @Test
    public void hostCreate(){
        HostCreateParam param=new HostCreateParam();
        param.setSpecifyCluster(""); //指定集群
        param.setHostname("test"); //宿主机名称
        param.setDescribe("");//描述
        param.setHostip("192.168.1.155"); //宿主机ip地址
        param.setPort(22); //端口
        param.setUsername("root"); //用户名
        param.setPassword("1234qwer"); //密码
        hostService.hostCreate(param);
    }

    //修改宿主机状态
    @Test
    public void updateHostStatus(){
        HostStatusUpdateParam param=new HostStatusUpdateParam();
        param.setHostip("192.168.1.154");
        param.setStatus(1);
        param.setUserUniqueId("");
        Status<String> status=hostService.hostUpdateStatus(param);
        System.out.println(status.getStatus());
    }

    //删除宿主机
    @Test
    public void deleteHost(){
        String ip="192.168.1.155";
        String userUniqueId="";
        Status<String> status=hostService.hostDelete(ip,userUniqueId);
        System.out.printf(status.getStatus());
    }

    //获取QPID消息队列
    @Test
    public void getQueueFromQpid(){
        QpidQueueParam param=new QpidQueueParam();
        param.setIp("192.168.1.10");
        param.setUsername("admin");
        param.setPassword("admin");
        param.setParams("id,name,type");
        List<QpidQueueDto> queues=hostService.getQueueFromQpid(param);
        System.out.println(queues);
    }
}
