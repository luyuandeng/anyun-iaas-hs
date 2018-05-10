package com.anyun.esb.component.registry.service.dao;

import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public interface DockerImageDao {

    /**
     * @param categoryType
     * @param templateName
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageDto<DockerImageDto> selectImageByConditions(String categoryType, String templateName, int pageNo, int pageSize);

    /**
     * @param id
     * @return
     */
    DockerImageDto selectById(String id);

    /**
     *
     * @param dockerImageDto
     * @return
     * @throws DaoException
     */
    DockerImageDto updateStatus(DockerImageDto dockerImageDto) throws DaoException;

    /**
     * @param ids
     * @param status
     * @throws DaoException
     */
    void batchUpdateStatus(List<String> ids, int status) throws DaoException;

    /**
     *
     * @param dto
     * @throws DaoException
     */
    void update(DockerImageDto dto) throws DaoException;

    /**
     *
     * @param category
     * @param name
     * @return
     */
    List<DockerImageDto> selectByFullName(String category, String name);

    /**
     *
     * @return DaoException
     */

    List<DockerImageDto> DockerImageQueryRegistry() throws DaoException;

    /**
     *
     * @param name
     * @return List<DockerImageDto>
     * @throws DaoException
     */
    List<DockerImageDto> selectIamgeByCategory(String name) throws DaoException;

    /**
     * 插入未注册Docker镜像
     * @param dto1
     * @throws DaoException
     */
    void insertUnregistDockerImages(DockerImageDto dto1) throws DaoException;

    /**
     * 查询未注册镜像
     * @return
     * @throws DaoException
     */
    List<DockerImageDto> selectUnregistDockerImages() throws DaoException;
}
