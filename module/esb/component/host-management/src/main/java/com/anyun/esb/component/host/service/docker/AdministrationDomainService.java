package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.param.DomainCreateParam;

/**
 * Created by twh-workspace on 17-2-22.
 */
public interface AdministrationDomainService {

    /**
     * 根据条件创建管理域
     * @param param
     * @throws Exception
     */
    void createAdministrationDomainByCondition(DomainCreateParam param)throws Exception;

    /**
     * 根据ID删除管理域
     * @param id
     */
    void administrationDomainDeleteById(String id);
}
