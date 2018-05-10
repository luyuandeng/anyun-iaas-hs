package com.anyun.esb.component.registry.service.dao;

import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public interface DockerImageCategoryDao {

    /**
     * @param id
     * @return
     */
     DockerImageCategoryDto selectById(String id);

    /**
     *
     * @param dockerImageCategoryDto
     * @return
     */
     DockerImageCategoryDto updateStatus(DockerImageCategoryDto dockerImageCategoryDto) throws DaoException;

    /**
     * @param ids
     * @throws Exception
     */
     void batchDockerImageCategoriesStatus(List<String> ids,int status);

    /**
     * @param dockerImageCategoryDto
     * @throws Exception
     */
    DockerImageCategoryDto update(DockerImageCategoryDto dockerImageCategoryDto);

    /**
     * @param
     * @throws Exception
     */
     List<DockerImageCategoryDto> selectRegistryCategories() throws DaoException;

    /**
     * 插入未注册镜像分类
     * @param dto1
     * @throws DaoException
     */
    void insertUnregistDockerImageCategories(DockerImageCategoryDto dto1) throws DaoException;

    /**
     * 查询所有镜像分类
     * @return
     * @throws DaoException
     */
    List<DockerImageCategoryDto> selectUnregistCategories() throws DaoException;


}
