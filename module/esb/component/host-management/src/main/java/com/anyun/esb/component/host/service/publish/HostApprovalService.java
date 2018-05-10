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

package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.HostApprovalDto;
import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.param.HostApprovalParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/27/16
 */
public class HostApprovalService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostApprovalService.class);

    private HostBaseInfoDao hostBaseInfoDao;
    private HostApprovalParam param;

    public HostApprovalService() {
        hostBaseInfoDao = new HostBaseInfoDaoImpl();
    }

    @Override
    public String getName() {
        return "host_approval";
    }

    @Override
    public String getDescription() {
        return "Host Approval Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);

        try {
            param = JsonUtil.fromJson(HostApprovalParam.class, postBody);
            if (param == null)
                throw new Exception();
        } catch (Exception ex) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + ex + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        if (param.getStatus() < 1) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("宿主机审批请求状态必须为[>0]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        HostBaseInfoDto hostBaseInfoDto = hostBaseInfoDao.selectById(param.getId());
        if (hostBaseInfoDto == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Host [" + param.getId() + "] not exist");
            exception.setUserMessage("宿主机[" + param.getId() + "]不存在");
            exception.setUserTitle("宿主机不存在");
            throw exception;
        }
        if (hostBaseInfoDto.getStatus() != 9) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Host [" + param.getId() + "] are approvaled");
            exception.setUserMessage("宿主机[" + param.getId() + "]已经审批过");
            exception.setUserTitle("宿主机已经审批过");
            throw exception;
        }
        hostBaseInfoDto.setStatus(param.getStatus());
        if (param.getStatus() == 1)
            hostBaseInfoDto.setDescript(param.getDescript());
        HostApprovalDto hostApprovalDto = new HostApprovalDto();
        hostApprovalDto.setStatus(param.getStatus());
        hostApprovalDto.setReason(param.getReason());
        hostBaseInfoDto.setHostApprovalDto(hostApprovalDto);
        hostBaseInfoDao.approval(hostBaseInfoDto);
        return new Status<String>("success");
    }
}
