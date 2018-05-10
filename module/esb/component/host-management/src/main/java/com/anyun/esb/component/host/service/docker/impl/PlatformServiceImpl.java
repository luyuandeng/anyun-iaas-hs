package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.PlatformDto;
import com.anyun.cloud.param.PlatformCreateParam;
import com.anyun.cloud.param.PlatformUpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.dao.PlatformDao;
import com.anyun.esb.component.host.dao.impl.PlatformDaoImpl;
import com.anyun.esb.component.host.service.docker.PlatformService;
import com.anyun.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-10-14.
 */
public class PlatformServiceImpl implements PlatformService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PlatformServiceImpl.class);
    PlatformDao platformDao = new PlatformDaoImpl();

    @Override
    public void platformCreate(PlatformCreateParam param) throws Exception {
        PlatformDto platformDto = new PlatformDto();
        platformDto.setArea(param.getArea());
        platformDto.setDescription(param.getDescription());
        platformDto.setStatus(param.getStatus());
        platformDto.setIpDomain(param.getIpDomain());
        platformDto.setBaseUrl(param.getBaseUrl());
        platformDto.setPort(param.getPort());
        platformDto.setName(param.getName());
        String id = StringUtils.uuidGen();
        platformDto.setId(id);
        platformDao.insertPlatform(platformDto);
        if (param.getStatus() == 1)
            platformSetAsDefault(id);
    }

    @Override
    public void platformUpdate(PlatformUpdateParam param) throws Exception {
        PlatformDto platformDto = new PlatformDto();
        platformDto.setName(param.getName());
        platformDto.setDescription(param.getDescription());
        platformDto.setId(param.getId());
        platformDto.setIpDomain(param.getIpDomain());
        platformDto.setPort(param.getPort());
        platformDto.setBaseUrl(param.getBaseUrl());
        platformDto.setArea(param.getArea());
        platformDto.setStatus(param.getStatus());
        platformDao.updatePlatform(platformDto);
        if (param.getStatus() == 1)
            platformSetAsDefault(param.getId());
    }

    @Override
    public void platformDelete(String id) throws Exception {
        platformDao.deletePlatform(id);
    }

    @Override
    public PlatformDto platformQueryById(String id) throws DaoException {
        return platformDao.selectPlatformById(id);
    }

    @Override
    public List<PlatformDto> platformQueryAll() throws Exception {
        return platformDao.selectPlatformAll();
    }

    @Override
    public PlatformDto platformQueryDefault() throws Exception {
        List<PlatformDto> l = platformDao.selectPlatformByStatus(1);
        if (l != null && l.size() > 0) {
            return l.get(0);
        }

        if (platformQueryAll() != null && platformQueryAll().size() > 0) {
            return platformQueryAll().get(0);
        }
        return null;
    }

    @Override
    public void platformSetAsDefault(String id) throws Exception {
        PlatformDto platformDto = platformQueryById(id);
        if (platformDto == null || StringUtils.isEmpty(platformDto.getId()))
            throw new Exception("The platform does not exists");
        if (platformDto.getStatus() != 1)
            platformDao.updatePlatformByStatusAndId(1, id);
        PlatformDto dto = platformQueryById(id);
        if (dto != null && dto.getStatus() == 1)
            platformDao.updatePlatformByCondition(0, id);
    }

    @Override
    public List<PlatformDto> logicCenterQuery(String subMethod, String subParameters) throws Exception {
        List<PlatformDto> l = null;
        switch (subMethod) {
            case "QUERY_BY_ID":
                if (StringUtils.isEmpty(subParameters))
                    throw new Exception("subParameters is empty");
                PlatformDto p = platformQueryById(subParameters);
                if (p != null) {
                    l = new ArrayList<>();
                    l.add(p);
                }
                break;

            case "QUERY_A_DEFAULT":
                PlatformDto pl = platformQueryDefault();
                if (pl != null) {
                    l = new ArrayList<>();
                    l.add(pl);
                }
                break;

            case "QUERY_ALL":
                l = platformQueryAll();
                break;

            default:
                throw new Exception("subMethod" + subMethod + "  does not exist");
        }
        return l;
    }
}
