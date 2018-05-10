package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.NetL2InfoDto;
import com.anyun.cloud.param.NetL2CreateParam;
import com.anyun.cloud.param.NetL2UpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.dao.NetL2InfoDao;
import com.anyun.esb.component.host.dao.impl.NetL2InfoDaoImpl;
import com.anyun.esb.component.host.service.docker.NetL2InfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by sxt on 17-4-10.
 */
public class NetL2InfoServiceImpl implements NetL2InfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetL2InfoServiceImpl.class);
    private NetL2InfoDao   netL2InfoDao  =new NetL2InfoDaoImpl();


    @Override
    public NetL2InfoDto queryNetL2InfoById(String id) throws Exception {
        return  netL2InfoDao.selectNetL2InfById(id);
    }

    @Override
    public List<NetL2InfoDto> getNetL2InfoListByType(String type) throws Exception {
        return netL2InfoDao.selectNetL2ListByType(type);
    }

    @Override
    public void netL2DeleteById(String id) throws Exception {
        netL2InfoDao.deleteNetL2ById(id);
    }

    @Override
    public NetL2InfoDto createNetL2(NetL2CreateParam param) throws Exception {
        String id = StringUtils.uuidGen();
        NetL2InfoDto dto = new NetL2InfoDto();
        dto.setId(id);
        dto.setType(param.getType());
        dto.setName(param.getName());
        dto.setCluster(param.getCluster());
        dto.setDescription(param.getDescription());
        dto.setPhysicalInterface(param.getPhysical_interface());
        return  netL2InfoDao.createNetL2(dto);
    }

    @Override
    public NetL2InfoDto updateNetL2(NetL2UpdateParam param) {
        NetL2InfoDto dto = new NetL2InfoDto();
        dto.setId(param.getId());
        dto.setType(param.getType());
        dto.setName(param.getName());
        dto.setCluster(param.getCluster());
        dto.setDescription(param.getDescription());
        dto.setPhysicalInterface(param.getPhysical_interface());
        return netL2InfoDao.updateNetL2(dto);
    }

}
