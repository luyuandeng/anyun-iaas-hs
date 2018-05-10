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

import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;

import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/27/16
 */
public class QueryHostByStatusService extends AbstractBusinessService {
    private HostBaseInfoDao hostBaseInfoDao;

    public QueryHostByStatusService() {
        hostBaseInfoDao = new HostBaseInfoDaoImpl();
    }

    @Override
    public String getName() {
        return "host_query_by_status";
    }

    @Override
    public String getDescription() {
        return "Query Host By Status Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String statusValue = exchange.getIn().getHeader("status", String.class);
        if (StringUtils.isEmpty(statusValue)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param [status] not exist");
            exception.setUserMessage("服务参数 [status] 不存在");
            exception.setUserTitle("参数验证失败");
        }
        int status = -1;
        try {
            status = Integer.valueOf(statusValue);
            if (status != 0 || status != 1 || status != 9)
                throw new Exception();
        } catch (Exception ex) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param [status] format error");
            exception.setUserMessage("服务参数 [status] 格式化错误");
            exception.setUserTitle("参数验证失败");
        }
        List<HostBaseInfoDto> hostBaseInfoDtos = hostBaseInfoDao.selectByStatus(status);
        return hostBaseInfoDtos;
    }
}
