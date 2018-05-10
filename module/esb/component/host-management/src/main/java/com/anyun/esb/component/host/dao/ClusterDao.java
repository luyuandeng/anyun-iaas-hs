package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.ClusterDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by gp on 17-3-28.
 */
public interface ClusterDao {
    /**
     * 查询所有集群
     * @return
     */
    List<ClusterDto> queryAllCluster();

    /**
     * 根据状态查询集群
     * @param status
     * @return
     */
    List<ClusterDto> selectClusterByStatus(String status);

    /**
     * 根据类型查询集群
     * @param type
     * @return
     */
    List<ClusterDto> selectClusterByType(String type);

    /**
     * 根据状态和类型查询集群
     * @param type
     * @param status
     * @return
     */
    List<ClusterDto> selectClusterByTypeAndStatus(String type, String status);

    /**
     * 根据id查询集群
     * @param id
     * @return
     */
    ClusterDto selectClusterById(String id);

    /**
     * 创建集群
     * @param dto
     * @throws DaoException
     */
    ClusterDto createCluster(ClusterDto dto) throws DaoException;

    /**
     * 更新集群
     * @param dto
     * @throws DaoException
     */
    ClusterDto updateCluster(ClusterDto dto) throws DaoException;

    /**
     * 删除集群
     * @param id
     * @throws DaoException
     */
    void deleteCluster(String id) throws DaoException;

    /**
     * 更改集群状态
     * @param dto
     * @throws DaoException
     */
    void changeClusterStatus(ClusterDto dto) throws DaoException;
}
