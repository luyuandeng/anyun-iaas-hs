package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.ClusterDto;
import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.param.ClusterChangeStatusParam;
import com.anyun.cloud.param.ClusterCreateParam;
import com.anyun.cloud.param.ClusterUpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.dao.ClusterDao;
import com.anyun.esb.component.host.dao.impl.ClusterDaoImpl;
import com.anyun.esb.component.host.service.docker.ClusterService;
import com.anyun.esb.component.host.service.docker.HostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gp on 17-3-28.
 */
public class ClusterServiceImpl implements ClusterService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClusterServiceImpl.class);
    private ClusterDao clusterDao = new ClusterDaoImpl();

    @Override
    public List<ClusterDto> queryClusterByStatus(String status) {
        return clusterDao.selectClusterByStatus(status);
    }

    @Override
    public List<ClusterDto> queryClusterByType(String type) {
        return clusterDao.selectClusterByType(type);
    }

    @Override
    public List<ClusterDto> queryClusterByTypeAndStatus(String type, String status) {
        return clusterDao.selectClusterByTypeAndStatus(type, status);
    }

    @Override
    public ClusterDto queryClusterById(String id) {
        return clusterDao.selectClusterById(id);
    }

    @Override
    public ClusterDto createCluster(ClusterCreateParam param) throws Exception {
        String id = StringUtils.uuidGen();
        ClusterDto dto = new ClusterDto();
        dto.setId(id);
        dto.setName(param.getName());
        dto.setDescription(param.getDescription());
        dto.setStatus(param.getStatus());
        dto.setType("Docker");
        return clusterDao.createCluster(dto);
    }

    @Override
    public ClusterDto updateCluster(ClusterUpdateParam param) throws Exception {
        ClusterDto dto = new ClusterDto();
        dto.setId(param.getId());
        dto.setName(param.getName());
        dto.setDescription(param.getDescription());
        dto.setStatus(param.getStatus());
        ClusterDto clusterDto = clusterDao.updateCluster(dto);
        return clusterDto;
    }

    @Override
    public void deleteCluster(String id) throws Exception {
        //查询集群下所有宿主
        HostService hostService = new HostServiceImpl();
        List<HostBaseInfoDto> l = hostService.queryHostByCluster(id);
        LOGGER.debug("The number of hosts in the cluster:[{}] is :[{}]", id, l.size());
        for (HostBaseInfoDto h : l) {
            if (StringUtils.isEmpty(h.getId()))
                continue;
            //删除宿主机器
            hostService.deleteHost(h.getId(),h.getDescript());
            LOGGER.debug("Host:[{}]  delete  success!!!", h.getId());
        }
        //从表中删除集群
        clusterDao.deleteCluster(id);
    }

    @Override
    public void changeClusterStatus(ClusterChangeStatusParam param) throws Exception {
        ClusterDto dto = new ClusterDto();
        dto.setId(param.getId());
        dto.setStatus(param.getStatus());
        clusterDao.changeClusterStatus(dto);
    }
}
