package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.PictureDto;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.dto.ProjectInfoQueryDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ProjectCreateParam;
import com.anyun.cloud.param.ProjectUpdateParam;

import java.util.List;

/**
 * Created by sxt on 16-7-12.
 */
public interface ProjectService {
    ProjectDto createProject(ProjectCreateParam param) throws Exception;

    ProjectDto updateProject(ProjectUpdateParam param) throws Exception;

    void deleteProject(String id) throws Exception;

    List<ProjectDto> queryProjectByCondition(String userUniqueId) throws Exception;

    ProjectDto queryProjectById(String id) throws Exception;

    ProjectInfoQueryDto projectInfoQueryById(String id) throws Exception;

    List<ProjectDto> projectQuery(String subMethod, String subParameters) throws Exception;

    PageDto<ProjectDto> getPageListConditions(CommonQueryParam param)throws Exception;

    ProjectDto queryProjectDtoByPlatFormNetworkId(String label) throws Exception;

    List<ProjectDto> queryProjectByCondition() throws Exception;
}
