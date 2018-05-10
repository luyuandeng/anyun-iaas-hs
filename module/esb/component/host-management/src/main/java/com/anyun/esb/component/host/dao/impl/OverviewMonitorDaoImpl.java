package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.OverviewMonitorDao;
import com.anyun.exception.DaoException;

import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-9-13.
 */
public class OverviewMonitorDaoImpl extends BaseMyBatisDao implements OverviewMonitorDao{

    /**
     * 总览监控--容器数量
     * @return
     * @throws DaoException
     */
    @Override
    public Map<String,Object> selectContainerCount() throws DaoException {
        String sql = "dao.OverviewDao.selectContainerCount";
        return selectOne(Map.class,sql,"");
    }

    /**
     * 据宿主机查询容器
     * @param host
     * @return
     * @throws DaoException
     */
    @Override
    public Map<String, Object> selectContainerByHost(String host) throws DaoException {
        String sql="dao.OverviewDao.selectContainerByHost";
        return selectOne(Map.class,sql,host);
    }

    /**
     * 根据宿主机ID查询容器详情
     * @param hostid
     * @return
     * @throws DaoException
     */
    @Override
    public List<ContainerDto> selectContainerInfoByHostId(String hostid) throws DaoException {
        String sql ="dao.OverviewDao.selectContainerInfoByHostId";
        return selectList(ContainerDto.class,sql,hostid);
    }

    /**
     * 总览监控--卷数量
     * @return
     * @throws DaoException
     */
    @Override
    public Map<String, Object> selectVolumeCount() throws DaoException {
        String sql = "dao.OverviewDao.selectVolumeCount";
        return selectOne(Map.class,sql,"");
    }
}
