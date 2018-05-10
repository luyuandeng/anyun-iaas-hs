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
package test.com.anyun.host;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.HostAndIpDto;
import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.dto.HostCpuInfoDto;
import com.anyun.cloud.dto.HostMemoryInfoDto;
import com.anyun.cloud.param.HostApprovalParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.JbiCommon;
import com.anyun.common.jbi.component.ZKConnector;
import com.anyun.common.jbi.component.ZkConnectLostListener;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.service.publish.*;
import com.anyun.exception.JbiComponentException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 5/9/16
 */
public class HostServiceTest extends BaseComponentTest {
    private HostApprovalService hostApprovalService;
    private HostCpuInfoQueryService hostCpuInfoQueryService;
    private HostMemoryInfoQueryService hostMemoryInfoQueryService;
    private HostInfoQueryByIdService hostInfoQueryByIdService;
    private CALLHostInfoQueryAllActiveService callHostInfoQueryAllActiveService;

    @Before
    public void setUp() throws Exception {
        String hostCert = "/home/sxt/.ssh/office_rsa";
        HostSshClient hostSshClient = new HostSshClient(hostCert);
        hostSshClient.setHost("192.168.1.154");
        hostSshClient.connect();
        String serialNumber = "5A1035E96D7F35B945259852F9821A28";
        Env.export("host.ssh." + serialNumber, hostSshClient);
        ZKConnector zookeeperConnector = new ZKConnector();
        zookeeperConnector.setConnectLostListener(new ZkConnectLostListener() {
            @Override
            public void afterConnectLost(ZKConnector connector) {
            }
        });
        String zkString = "192.168.1.117:2181";
        zookeeperConnector.connect(zkString, 3000);
        Env.set(ZKConnector.class, zookeeperConnector);
        hostApprovalService = new HostApprovalService();
        hostCpuInfoQueryService = new HostCpuInfoQueryService();
        hostMemoryInfoQueryService = new HostMemoryInfoQueryService();
        hostInfoQueryByIdService = new HostInfoQueryByIdService();
        callHostInfoQueryAllActiveService=new CALLHostInfoQueryAllActiveService();
    }

    @Test
    public void testHostApprovalService() throws Exception {
        try {
            HostApprovalParam hostApprovalParam = new HostApprovalParam();
            hostApprovalParam.setId("F5B82DE1AC479A987151F83156EC5D11");
            hostApprovalParam.setDescript("宿主机154");
            hostApprovalParam.setReason("审核通过");
            hostApprovalParam.setStatus(1);
            exchange.getIn().setBody(hostApprovalParam.asJson());
            Status<String> status = (Status<String>) hostApprovalService.process(endpoint, exchange);
            System.out.println(JbiCommon.toJson(status));
        } catch (JbiComponentException ex) {
            System.err.println(ex.getUserMessage());
            ex.printStackTrace();
        }
    }

    @Test
    public void testHostTestQuery() throws Exception {
        HostApprovalParam hostApprovalParam = new HostApprovalParam();
        hostApprovalParam.setId("EAF7AAF0934E87CC29BC6ACEB2365605");
        hostApprovalParam.setDescript("测试的宿主机");
        hostApprovalParam.setReason("审核不通过");
        hostApprovalParam.setStatus(8);
        System.out.println(hostApprovalParam.asJson());
        TestHostQueryService service = new TestHostQueryService();
        exchange.getIn().setBody(hostApprovalParam.asJson());
        List<HostBaseInfoDto> dtos = (List<HostBaseInfoDto>) service.process(endpoint, exchange);
        for (HostBaseInfoDto dto : dtos) {
            System.out.println(JbiCommon.toJson(dto));
        }
    }

    // 获取 CPU 信息
    @Test
    public void testQueryHostCpuInfoQuery() {
        HostCpuInfoDto hostCpuInfoDto = (HostCpuInfoDto) hostCpuInfoQueryService.process(endpoint, exchange);
        System.out.println(hostCpuInfoDto.getCpuFamily());
        System.out.println(hostCpuInfoDto.getCpuCoreLimit());
    }


    //获取内存信息
    @Test
    public void testHostMemoryInfoQuery() {
        List<HostMemoryInfoDto> l = (List<HostMemoryInfoDto>) hostMemoryInfoQueryService.process(endpoint, exchange);
        for (HostMemoryInfoDto dto : l) {
            System.out.println("memoryLimit:" + dto.getMemoryLimit() + "&" + "memorySwapLimit:" + dto.getMemorySwapLimit());
        }
    }


    //根据id查询宿主机信息hostApprovalService
    @Test
    public void testHostInfoQueryById() {
        exchange.getIn().setHeader("id", "5A1035E96D7F35B945259852F9821A28");
        HostBaseInfoDto dto = (HostBaseInfoDto) hostInfoQueryByIdService.process(endpoint, exchange);
        System.out.println(JsonUtil.toJson(dto));
    }


    //查询所有action 宿主机
    @Test
    public void testHostInfoQueryAllActiveService() {
        String s = (String) callHostInfoQueryAllActiveService.process(endpoint, exchange);
        System.out.println(s);
    }

}
