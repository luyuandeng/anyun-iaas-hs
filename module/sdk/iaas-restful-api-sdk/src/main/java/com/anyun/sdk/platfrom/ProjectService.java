package com.anyun.sdk.platfrom;


import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.dto.ProjectInfoQueryDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ProjectCreateParam;
import com.anyun.cloud.param.ProjectUpdateParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by sxt on 16-7-13.
 */
public interface ProjectService {

    /**
     * 根据条件查询项目列表
     *
     * @return ProjectDto
     * @throws RestfulApiException
     */
    List<ProjectDto> queryProjectByCondition(String userUniqueId) throws RestfulApiException;

    /**
     * 创建项目
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    ProjectDto createProject(ProjectCreateParam param) throws RestfulApiException;

    /**
     * 根据 id 修改项目
     *
     * @param param
     * @throws RestfulApiException
     */
    ProjectDto updateProject(ProjectUpdateParam param) throws RestfulApiException;

    /**
     * 根据 id 删除项目
     *
     * @param id
     * @param userUniqueId
     * @throws RestfulApiException
     */
    Status<String> deleteProjectByProjectId(String id,String userUniqueId) throws RestfulApiException;

    /**
     * 根据id 查询网络和容器关联信息
     *
     * @param id
     * @param userUniqueId
     * @return ProjectInfoQueryDto
     */
    ProjectInfoQueryDto queryProjectInfoById(String id,String userUniqueId) throws RestfulApiException;

    /**
     * 根据id 查询项目
     *
     * @param id
     * @return ProjectDto
     */
    ProjectDto queryProjectById(String id,String userUniqueId) throws RestfulApiException;

    /**
     * @param userUniqueId
     * @parm subMethod
     * @param subParameters
     * @return List<PlatformDto>
     * @throws RestfulApiException
     */
    List<ProjectDto> projectQuery(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException;

    PageDto<ProjectDto> getPageListConditions(CommonQueryParam param)throws RestfulApiException;
}
