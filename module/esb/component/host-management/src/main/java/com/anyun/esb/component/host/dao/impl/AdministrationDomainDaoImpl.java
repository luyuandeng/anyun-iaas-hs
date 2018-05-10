package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.DomainDto;
import com.anyun.esb.component.host.dao.AdministrationDomainDao;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by twh-workspace on 17-2-22.
 */
public class AdministrationDomainDaoImpl extends BaseMyBatisDao implements AdministrationDomainDao {

    @Override
    public void insertAdministrationDomain(DomainDto domainDto) {
        String sql = "dao.AdministrationDomainDao.insertAdministrationDomain";
        insert(sql,domainDto);
    }

    @Override
    public void deleteadministrationDomainById(String id) {
        String sql = "dao.AdministrationDomainDao.deleteAdministrationDomainById";
        //Map<String, Object> m = new HashMap<>();
        //m.put("id", id);
        delete(sql,id);
    }
}
