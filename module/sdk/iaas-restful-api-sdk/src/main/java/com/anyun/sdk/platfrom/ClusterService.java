package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ClusterDto;
import com.anyun.cloud.param.ClusterChangeStatusParam;
import com.anyun.cloud.param.ClusterCreateParam;
import com.anyun.cloud.param.ClusterUpdateParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import java.util.List;

/**
 * Created by jt-workspace on 17-4-6.
 */
public interface ClusterService {

    /**
     * 1、查询集群详情
     *
     * @param id           String   true
     * @param userUniqueId
     * @retuen
     */
    ClusterDto ClusterQueryById (String id, String userUniqueId)throws RestfulApiException;

    /**
     * 2、查询集群列表
     *
     * @param userUniqueId
     * @param status
     * @retuen
     */
    List<ClusterDto> ClusterQueryListService (String status, String userUniqueId) throws RestfulApiException;

    /**
     * 3、删除集群
     *
     * @param id           String  true
     * @param userUniqueId
     * @retuen Status<String>
     */
    Status<String> ClusterDeleteService (String id, String userUniqueId)throws RestfulApiException;


    /**
     * 4、创建集群
     *
     * @param
     * @param
     * @retuen
     */

    ClusterDto ClusterCreateService (ClusterCreateParam param)throws RestfulApiException;

    /**
     * 5、修改集群
     *
     * @param
     * @param
     * @retuen Status<String>
     */
    ClusterDto ClusterUpdateService (ClusterUpdateParam param)throws RestfulApiException;

    /**
     * 6、更改状态
     *
     * @param
     * @param
     * @retuen Status<String>
     */
    Status<String> changeStatus(ClusterChangeStatusParam param)throws RestfulApiException;
}
