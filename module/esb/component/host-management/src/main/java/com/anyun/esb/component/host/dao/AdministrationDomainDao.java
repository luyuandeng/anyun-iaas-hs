package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.DomainDto;

/**
 * Created by twh-workspace on 17-2-22.
 */
public interface AdministrationDomainDao {
    /**
     * 根据条件添加管理域
     * @param domainDto
     */
    void insertAdministrationDomain(DomainDto domainDto);

    /**
     * 根据ID删除管理域
     * @param id
     */
    void deleteadministrationDomainById(String id);
}
