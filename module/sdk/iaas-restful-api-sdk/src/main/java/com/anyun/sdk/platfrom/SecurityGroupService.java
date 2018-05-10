package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.SecurityGroupDto;
import com.anyun.cloud.param.*;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by sxt on 16-10-19.
 */
public interface SecurityGroupService {
    /**
     * 创建一个安全组
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    SecurityGroupDto securityGroupCreate(SecurityGroupCreateParam param) throws RestfulApiException;

    /**
     * 修改安全组
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    SecurityGroupDto securityGroupUpdate(SecurityGroupUpdateParam param) throws RestfulApiException;

    /**
     * 根据label 删除 安全组
     *
     * @param label
     * @param userUniqueId
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> securityGroupDeleteByLabel(String label,String userUniqueId) throws RestfulApiException;

    /**
     * 根据label 查询安全组
     *
     * @param label
     * @param userUniqueId
     * @return SecurityGroupDto
     * @throws RestfulApiException
     */
    SecurityGroupDto securityGroupQueryByLabel(String label,String userUniqueId) throws RestfulApiException;

    /**
     * 根据project 查询安全组
     *
     * @param project
     * @param userUniqueId
     * @return List<SecurityGroupDto>
     * @throws RestfulApiException
     */
    List<SecurityGroupDto> securityGroupQueryByProject(String project,String userUniqueId) throws RestfulApiException;

    /**
     * 添加 ip 到安全组
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> addIpToSecurityGroup(AddIpToSecurityGroupParam param) throws RestfulApiException;


    /**
     * 移除 ip 从安全组移除
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> removeIpFromSecurityGroup(RemoveIpFromSecurityGroupParam param) throws RestfulApiException;

    /**
     * 查询安全组
     * @param userUniqueId
     * @param subMethod
     * @param subParameters
     * @return
     * @throws RestfulApiException
     */
    List<SecurityGroupDto> querySecurityGroup(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException;

    /**
     * 根据条件查询安全组
     * @param param
     * @return
     * @throws RestfulApiException
     */
    PageDto<SecurityGroupDto> getPageListConditions(CommonQueryParam param)throws RestfulApiException;

}
