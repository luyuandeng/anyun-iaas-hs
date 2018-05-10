package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.ClusterDto;
import com.anyun.cloud.param.ClusterChangeStatusParam;
import com.anyun.cloud.param.ClusterCreateParam;
import com.anyun.cloud.param.ClusterUpdateParam;

import java.util.List;

/**
 * Created by gp on 17-3-28.
 */
public interface ClusterService {
    /**
     * 根据状态查询集群
     * @param status
     * @return
     */
    List<ClusterDto> queryClusterByStatus(String status) throws Exception;

    /**
     * 根据具类型查询集群
     * @param type
     * @return
     */
    List<ClusterDto> queryClusterByType(String type);

    /**
     * 根据状态和类型查询集群
     * @param type
     * @param status
     * @return
     */
    List<ClusterDto> queryClusterByTypeAndStatus(String type, String status);

    /**
     * 根据id查询集群
     * @param id
     * @return
     */
    ClusterDto queryClusterById(String id) throws Exception;

    /**
     * 创建集群
     * @param param
     * @throws Exception
     */
    ClusterDto createCluster(ClusterCreateParam param) throws Exception;

    /**
     * 更新集群
     * @param param
     * @throws Exception
     */
    ClusterDto updateCluster(ClusterUpdateParam param) throws Exception;

    /**
     * 删除集群
     * @param id
     * @throws Exception
     */
    void deleteCluster(String id) throws Exception;

    /**
     * 更改集群状态
     * @param param
     */
    void changeClusterStatus(ClusterChangeStatusParam param) throws Exception;
}
