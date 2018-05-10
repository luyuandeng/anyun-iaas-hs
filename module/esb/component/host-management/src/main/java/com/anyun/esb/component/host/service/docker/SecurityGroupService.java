package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.SecurityGroupDto;
import com.anyun.cloud.dto.SecurityGroupIpDto;
import com.anyun.cloud.param.*;

import java.util.List;

/**
 * Created by sxt on 16-10-17.
 */
public interface SecurityGroupService {
    SecurityGroupDto securityGroupCreate(SecurityGroupCreateParam param) throws  Exception;

    SecurityGroupDto securityGroupUpdate(SecurityGroupUpdateParam param) throws  Exception;

    SecurityGroupDto securityGroupQueryByLabel(String label) throws  Exception;

    List<SecurityGroupDto> securityGroupQueryByProject(String project) throws  Exception;

    void securityGroupDeleteByLabel(String label) throws  Exception;

    void AddIpToSecurityGroup(AddIpToSecurityGroupParam param) throws  Exception;

    void removeIpFromSecurityGroup(RemoveIpFromSecurityGroupParam param) throws Exception;

    /**
     * 查询安全组
     * @param userUniqueId
     * @param subMethod
     * @param subParameters
     * @return
     */
    List<SecurityGroupDto> securityGroupQuery(String userUniqueId, String subMethod, String subParameters) throws Exception;

    /**
     * 根据条件查询安全组
     * @param commonQueryParam
     * @return
     */
    PageDto<SecurityGroupDto> queryByConditions(CommonQueryParam commonQueryParam) throws Exception;

    SecurityGroupIpDto querySecurityGroupIpDtoBySecurityGroupIdAndContainerNetIpId(String securityGroupId, String containerNetIpId) throws Exception;
}
