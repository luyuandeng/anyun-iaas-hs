package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.exception.DaoException;

import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-9-13.
 */
public interface OverviewMonitorDao {
    /**
     * 总览监控--容器数量
     * @return
     * @throws DaoException
     */
    Map<String,Object> selectContainerCount() throws DaoException;

    /**
     * 据宿主机查询容器
     * @param host
     * @return
     * @throws DaoException
     */
    Map<String,Object> selectContainerByHost(String host) throws DaoException;

    /**
     * 根据宿主机ID查询容器详情
     * @param hostid
     * @return
     * @throws DaoException
     */
    List<ContainerDto> selectContainerInfoByHostId(String hostid) throws DaoException;

    /**
     * 总览监控--卷数量
     * @return
     * @throws DaoException
     */
    Map<String,Object> selectVolumeCount() throws DaoException;
}
