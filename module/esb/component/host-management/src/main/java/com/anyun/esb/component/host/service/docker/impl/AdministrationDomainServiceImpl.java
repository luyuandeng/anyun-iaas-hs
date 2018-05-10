package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.DomainDto;
import com.anyun.cloud.param.DomainCreateParam;
import com.anyun.esb.component.host.dao.AdministrationDomainDao;
import com.anyun.esb.component.host.dao.impl.AdministrationDomainDaoImpl;
import com.anyun.esb.component.host.service.docker.AdministrationDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by twh-workspace on 17-2-22.
 */
public class AdministrationDomainServiceImpl implements AdministrationDomainService {
    final static Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceImpl.class);
    private AdministrationDomainDao administrationDomainDao = new AdministrationDomainDaoImpl();
    /**
     * 根据条件创建管理域
     * @param param
     * @throws Exception
     */
    @Override
    public void createAdministrationDomainByCondition(DomainCreateParam param) throws Exception {
        DomainDto domainDto = new DomainDto();
        domainDto.setId(param.getId());
        domainDto.setDomain(param.getDomain());
        domainDto.setIp(param.getIp());
        domainDto.setContainerId(param.getContainerId());
        //当前时间
        Date currentDate = new Date();
        domainDto.setCreateTime(currentDate);
        domainDto.setLastModifyTime(currentDate);

        //将数据保存到数据库
        administrationDomainDao.insertAdministrationDomain(domainDto);
    }

    @Override
    public void administrationDomainDeleteById(String id) {
        administrationDomainDao.deleteadministrationDomainById(id);
    }
}
