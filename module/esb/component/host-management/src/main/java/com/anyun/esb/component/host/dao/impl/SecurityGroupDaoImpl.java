package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.SecurityGroupCallDto;
import com.anyun.cloud.dto.SecurityGroupDto;
import com.anyun.cloud.dto.SecurityGroupIpDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.SecurityGroupDao;
import com.anyun.exception.DaoException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-10-17.
 */
public class SecurityGroupDaoImpl extends BaseMyBatisDao implements SecurityGroupDao {
    @Override
    public SecurityGroupDto securityGroupInsert(SecurityGroupDto securityGroupDto) throws DaoException {
        String sql = "dao.SecurityGroupDao.securityGroupInsert";
        securityGroupDto.setCreateDate(new Date());
        insert(sql, securityGroupDto);
        return securityGroupSelectByLabel(securityGroupDto.getLabel());
    }

    @Override
    public SecurityGroupDto securityGroupSelectByProjectAndPort(String project, String port) throws DaoException {
        String sql = "dao.SecurityGroupDao.securityGroupSelectByProjectAndPort";
        Map<String, Object> m = new HashMap<>();
        m.put("port", port);
        m.put("project", project);
        return selectOne(SecurityGroupDto.class, sql, m);
    }

    @Override
    public SecurityGroupDto securityGroupUpdate(SecurityGroupDto securityGroupDto) throws DaoException {
        String sql = "dao.SecurityGroupDao.securityGroupUpdate";
        update(sql, securityGroupDto);
        return securityGroupSelectByLabel(securityGroupDto.getLabel());
    }

    @Override
    public SecurityGroupDto securityGroupSelectByLabel(String label) throws DaoException {
        String sql = "dao.SecurityGroupDao.securityGroupSelectByLabel";
        Map<String, Object> m = new HashMap<>();
        m.put("label", label);
        return selectOne(SecurityGroupDto.class, sql, m);
    }

    @Override
    public List<SecurityGroupDto> securityGroupSelectByProject(String project) throws DaoException {
        String sql = "dao.SecurityGroupDao.securityGroupSelectByProject";
        Map<String, Object> m = new HashMap<>();
        m.put("project", project);
        return selectList(SecurityGroupDto.class, sql, m);
    }

    @Override
    public void securityGroupDeleteByLabel(String label) throws DaoException {
        String sql = "dao.SecurityGroupDao.securityGroupDeleteByLabel";
        Map<String, Object> m = new HashMap<>();
        m.put("label", label);
        delete(sql, m);
    }

    @Override
    public void securityGroupIpInsert(SecurityGroupIpDto securityGroupIpDto) throws DaoException {
        String sql = "dao.SecurityGroupDao.securityGroupIpInsert";
        insert(sql, securityGroupIpDto);
    }

    @Override
    public void securityGroupIpDelete(SecurityGroupIpDto securityGroupIpDto) throws DaoException {
        String sql = "dao.SecurityGroupDao.securityGroupIpDelete";
        delete(sql, securityGroupIpDto);
    }

    @Override
    public List<SecurityGroupCallDto> selectHostIp() throws DaoException {
        String sql = "dao.SecurityGroupDao.selectHostIp";
        return selectList(SecurityGroupCallDto.class, sql, "");
    }

    @Override
    public List<SecurityGroupDto> securityGroupQueryAll() throws DaoException {
        String sql = "dao.SecurityGroupDao.securityGroupSelectAll";
        return selectList(SecurityGroupDto.class, sql, "");
    }

    @Override
    public List<SecurityGroupIpDto> securityGroupIpQueryAll() throws DaoException {
        String sql = "dao.SecurityGroupDao.securityGroupIpQueryAll";
        return selectList(SecurityGroupIpDto.class, sql, "");
    }

    @Override
    public void deleteSecurityGroupIpByContainerNetId(String id) throws DaoException {
        String sql = "dao.SecurityGroupDao.deleteSecurityGroupIpByContainerNetId";
        Map<String, Object> m = new HashMap<>();
        m.put("containerNetIpId", id);
        delete(sql, m);
    }

    @Override
    public SecurityGroupIpDto selectSecurityGroupIpDtoBySecurityGroupIdAndContainerNetIpId(String securityGroupId, String containerNetIpId) throws DaoException {
        String sql = "dao.SecurityGroupDao.selectSecurityGroupIpDtoBySecurityGroupIdAndContainerNetIpId";
        Map<String, Object> params = new HashMap<>();
        params.put("securityGroupId", securityGroupId);
        params.put("containerNetIpId", containerNetIpId);
        return selectOne(SecurityGroupIpDto.class, sql, params);
    }

    @Override
    public List<SecurityGroupDto> selectSecurityGroupByContainerId(String containerId) throws DaoException {
        String sql="dao.SecurityGroupDao.selectSecurityGroupByContainerId";
        Map<String,Object>  params=new HashMap<>();
        params.put("container",containerId);
        return selectList(SecurityGroupDto.class,sql,params);
    }
}
