package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.ServiceOperationDto;
import com.anyun.common.jbi.component.ZKConnector;
import com.anyun.esb.component.host.service.docker.ServiceOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gp on 16-12-14.
 */
public class ServiceOperationServiceImpl implements ServiceOperationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceOperationServiceImpl.class);

    private ZKConnector zookeeperConnector;

    @Override
    public List<ServiceOperationDto> queryServiceOperationStatus() throws Exception {
        Jedis jedis = new Jedis("redis.dev.anyuncloud.com", 6379);
        String zkString = jedis.get("com.anyun.zookeeper.address");
        String timeout = jedis.get("com.anyun.zookeeper.timeout");
        zookeeperConnector = new ZKConnector();
        zookeeperConnector.connect(zkString, Long.valueOf(timeout).intValue());
        String zkPath = jedis.get("com.anyun.zookeeper.path.service");
        LOGGER.debug("zookeeper Path :"+zkPath);
        String newzk = zkPath.substring(0,zkPath.length()-1);
        LOGGER.debug("newzk :"+newzk);
        List<String> list=zookeeperConnector.getChildren(newzk);
        List<ServiceOperationDto> dtos =new ArrayList<ServiceOperationDto>();
        for (int i=0;i<list.size();i++){
            ServiceOperationDto dto = new ServiceOperationDto();
            dto.setServiceName(list.get(i));
            dtos.add(dto);
        }
        return dtos;
    }
}
