package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ApplicationInfoDto;
import com.anyun.cloud.dto.ApplicationInfoLoadDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.*;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by gp on 16-10-26.
 */
public interface ApplicationService {

    /**
     * 创建应用
     *
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    ApplicationInfoDto applicationCreate(ApplicationCreateParam param) throws RestfulApiException;

    /**
     * 删除应用
     *
     * @param id
     * @param userUniqueId
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> applicationDeleteById(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 根据id  查询应用
     *
     * @param id
     * @return userUniqueId
     * @throws RestfulApiException
     */
    ApplicationInfoDto applicationQueryById(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 根据项目  查询应用
     *
     * @param project
     * @param userUniqueId
     * @return List<ApplicationInfoDto>
     * @throws RestfulApiException
     */
    List<ApplicationInfoDto> applicationQueryByProject(String project, String userUniqueId) throws RestfulApiException;

    /**
     * 查询应用信息列表
     */
    PageDto<ApplicationInfoDto> applicationQueryByConditions(CommonQueryParam param) throws RestfulApiException;

    /**
     * 查询应用负载信息列表
     */
    PageDto<ApplicationInfoLoadDto> applicationLoadQueryByConditions(CommonQueryParam param) throws RestfulApiException;

    /**
     * 操作负载
     */
    Status<String> operationLoad(ContainerOpParam param) throws RestfulApiException;

    /**
     * 添加负载
     */
    List<ApplicationInfoLoadDto> addLoad(ApplicationLoadAddParam param) throws RestfulApiException;

    /**
     * 重新发布应用
     *
     * @param   id 应用Id
     * @return  ApplicationInfoDto
     * @throws RestfulApiException
     */
    ApplicationInfoDto  republish(String id) throws RestfulApiException;
}
