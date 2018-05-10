package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.InterfaceConfigDto;
import com.anyun.cloud.dto.LoggerDataDto;
import com.anyun.cloud.dto.LoggerDto;
import com.anyun.cloud.param.LoggerQueryParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.common.HostWebClient;

import com.anyun.esb.component.host.service.docker.InterfaceService;
import com.anyun.esb.component.host.service.docker.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-11-2.
 */
public class LoggerServiceImpl implements LoggerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerServiceImpl.class);
    private InterfaceService interfaceService = new InterfaceServiceImpl();

    /**
     * 日志查询
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public List<LoggerDto> queryLoggerByCondition(LoggerQueryParam param) throws Exception {
        LOGGER.debug("--------------------------start-------------------------------");
        LOGGER.debug("请求参数param："+ JsonUtil.toJson(param));
        HostWebClient.Configuration configuration = new HostWebClient.Configuration();
        String name = "logger";
        InterfaceConfigDto interfaceConfigDto =interfaceService.queryInterfaceConfig(name);
        configuration.setPlatformAddress(interfaceConfigDto.getIp());
        HostWebClient hostWebClient = HostWebClient.build(configuration);
        String hostIp=param.getIp().replace(".","&");
        LOGGER.debug("转义后的hostIp："+ JsonUtil.toJson(hostIp));
        Map<String, Object> m = new HashMap<String, Object>() {{
            put("hostip", hostIp);
            put("starttime",param.getStartTime());
            put("endtime",param.getEndTime());
            put("message",param.getKeyWord());
            put("source",param.getFileName());
            put("pagenum",param.getPageNum());
            put("pagesize",param.getPageCount());
        }};
        LOGGER.debug("请求参数m："+ m);
        String json = hostWebClient.get("/es/searchlog/",m);
        LOGGER.debug("Json length :[{}]", json.length());
        Map<String, Object> l = JsonUtil.fromJson(Map.class, json);
        LOGGER.debug("接收参数：{[]}",l);
        if (l == null || l.isEmpty()) {
            return new ArrayList<>();
        }
        List<LoggerDto> list = new ArrayList<LoggerDto>();
        //for (Map<String, Object> map : l) {
        LoggerDto dto = new LoggerDto();
        dto.setPageNum(l.get("pagenum")== null ? null : l.get("pagenum").toString());
        dto.setPageCount(l.get("count")== null ? null : l.get("count").toString());
        List<Map<String,Object>> data = (List<Map<String, Object>>) l.get("data");
        List<LoggerDataDto> dataDtos = new ArrayList<>();
        for (Map<String,Object> map1: data) {
            LoggerDataDto dataDto = new LoggerDataDto();
            dataDto.setIp(map1.get("host")== null ? null : map1.get("host").toString());
            dataDto.setSource(map1.get("source")==null ?null : map1.get("source").toString());
            dataDto.setMessage(map1.get("message")==null ?null : map1.get("message").toString());
            dataDto.setTimestamp(map1.get("timestamp")==null ? null : map1.get("timestamp").toString());
            dataDto.setHostName(map1.get("hostname")== null ? null : map1.get("hostname").toString());
            dataDtos.add(dataDto);
        }
        dto.setData(dataDtos);
        list.add(dto);
        //}
        LOGGER.debug("接收参数：{[]}",JsonUtil.toJson(list));
        return list;
    }
}
