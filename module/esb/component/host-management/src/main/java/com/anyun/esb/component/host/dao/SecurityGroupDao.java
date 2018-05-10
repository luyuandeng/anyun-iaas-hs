package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.SecurityGroupCallDto;
import com.anyun.cloud.dto.SecurityGroupDto;
import com.anyun.cloud.dto.SecurityGroupIpDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 16-10-17.
 */
public interface SecurityGroupDao {
    SecurityGroupDto securityGroupInsert(SecurityGroupDto securityGroupDto) throws DaoException;

    SecurityGroupDto securityGroupSelectByProjectAndPort(String project ,String port) throws DaoException;

    SecurityGroupDto securityGroupUpdate(SecurityGroupDto securityGroupDto) throws DaoException;

    SecurityGroupDto securityGroupSelectByLabel(String label) throws DaoException;

    List<SecurityGroupDto> securityGroupSelectByProject(String project) throws DaoException;

    void securityGroupDeleteByLabel(String label) throws DaoException;

    void securityGroupIpInsert(SecurityGroupIpDto securityGroupIpDto) throws DaoException;

    void securityGroupIpDelete(SecurityGroupIpDto securityGroupIpDto) throws DaoException;

    List<SecurityGroupCallDto> selectHostIp() throws DaoException;

    List<SecurityGroupDto> securityGroupQueryAll() throws DaoException;

    List<SecurityGroupIpDto> securityGroupIpQueryAll() throws DaoException;

    void deleteSecurityGroupIpByContainerNetId(String id) throws DaoException;

    SecurityGroupIpDto selectSecurityGroupIpDtoBySecurityGroupIdAndContainerNetIpId(String securityGroupId, String containerNetIpId) throws DaoException;;

    List<SecurityGroupDto> selectSecurityGroupByContainerId(String containerId) throws DaoException;
}
