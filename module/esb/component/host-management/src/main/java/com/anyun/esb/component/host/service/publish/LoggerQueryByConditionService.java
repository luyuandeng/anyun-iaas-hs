package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.LoggerDto;
import com.anyun.cloud.param.LoggerQueryParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.service.docker.LoggerService;
import com.anyun.esb.component.host.service.docker.impl.LoggerServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-11-2.
 */
public class LoggerQueryByConditionService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerQueryByConditionService.class);
    private LoggerService loggerService;
    private HostBaseInfoDao hostBaseInfoDao;

    public LoggerQueryByConditionService() {
        loggerService = new LoggerServiceImpl();
        hostBaseInfoDao = new HostBaseInfoDaoImpl();
    }

    @Override
    public String getName() {
        return "logger_query_by_condition";
    }

    @Override
    public String getDescription() {
        return "Query Logger By Condition";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        Map<String,Object> postBody = exchange.getIn().getHeaders();
        LoggerQueryParam param = new LoggerQueryParam();
        param.setIp(postBody.get("ip").toString());
        param.setKeyWord(postBody.get("keyword").toString());
        param.setFileName(postBody.get("filename").toString());
        param.setGrade(postBody.get("grade").toString());
        param.setStartTime(postBody.get("starttime").toString());
        param.setEndTime(postBody.get("endtime").toString());
        param.setPageNum(Integer.parseInt(postBody.get("pagenum").toString()));
        param.setPageCount(Integer.parseInt(postBody.get("pagecount").toString()));
        param.setUserUniqueId(postBody.get("userUniqueId").toString());
        LOGGER.debug("请求参数param:{}",param);

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        String hostip = hostBaseInfoDao.selectHostIdByIp(param.getIp());
        if (StringUtils.isEmpty(hostip)){
            LOGGER.debug("this host ip is not existence");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param[ip] is  empty");
            exception.setUserMessage("服务参数 [ip] 不存在");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try{
            List<LoggerDto> list = loggerService.queryLoggerByCondition(param);

            return list;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("根据 条件 查询日志 失败");
            throw exception;
        }


    }
}
