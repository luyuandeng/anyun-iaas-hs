package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.*;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-8-3.
 */
public interface ContainerService {
    /**
     * 启动容器
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> startContainer(ContainerStartParam param) throws RestfulApiException;

    /**
     * 停止容器
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> stopContainer(ContainerStopParam param) throws RestfulApiException;

    /**
     * 根据id 删除容器
     *
     * @param id
     * @param userUniqueId
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> deleteContainer(String id, String userUniqueId) throws RestfulApiException;


    /**
     * 根据 id 查询容器
     *
     * @param id
     * @param userUniqueId
     * @return ContainerDto
     * @throws RestfulApiException
     */
    ContainerDto queryContainerById(String id, String userUniqueId) throws RestfulApiException;


    /**
     * 根据镜像  查询容器信息
     *
     * @param id
     * @param userUniqueId
     * @return List<ContainerDto>
     * @throws RestfulApiException
     */
    List<ContainerDto> queryContainerByImage(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 根据网络标签 查询容器信息
     *
     * @param id
     * @param userUniqueId
     * @return List<ContainerDto>
     * @throws RestfulApiException
     */
    List<ContainerDto> queryContainerByNetLabel(String id, String userUniqueId) throws RestfulApiException;


    /**
     * 根据项目  查询容器信息
     *
     * @param id
     * @param userUniqueId
     * @return ContainerDto
     * @throws RestfulApiException
     */
    List<ContainerDto> queryContainerByProject(String id, String userUniqueId) throws RestfulApiException;


    /**
     * 根据条件创建容器
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    ContainerDto createContainerByCondition(ContainerCreateByConditionParam param) throws RestfulApiException;

    /**
     * 根据项目及类型查询容器
     *
     * @param project
     * @param type
     * @param userUniqueId
     * @return List<ContainerDto>
     * @throws RestfulApiException
     */
    List<ContainerDto> queryContainerByProjectAndType(String project, int type, String userUniqueId) throws RestfulApiException;


    /**
     * 查询  容器列表
     *
     * @param userUniqueId
     * @param subMethod
     * @return List<ContainerDto>
     * @throws RestfulApiException
     * @parm subParameters
     */
    List<ContainerDto> queryContainer(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException;

    /**
     * 查询容器列表
     *
     * @retuen List<ContainerDto>
     **/
    PageDto<ContainerDto> getContainerList(CommonQueryParam param) throws RestfulApiException;


    /**
     * 操作容器
     *
     * @param param
     * @retuen status
     **/
    Status<String> operationContainer(ContainerOpParam param) throws RestfulApiException;

    /**
     * 批量创建容器
     */
    List<ContainerDto> createContainer(List<ContainerCreateByConditionParam> param) throws RestfulApiException;


    /**
     * 容器更改计算方案
     */
    Status<String> changeCalculationScheme(ContainerChangeCalculationSchemeParam param) throws RestfulApiException;


    /**
     * 容器更改磁盘方案
     */
    Status<String> changeDiskScheme(ContainerChangeDiskSchemeParam param) throws RestfulApiException;
}
