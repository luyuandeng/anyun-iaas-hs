package com.anyun.esb.component.registry.service.core;

import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ImageCategoryRegistParam;
import com.anyun.cloud.param.ImageRegistParam;

import java.util.List;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/4/16
 */
public interface RegistryService {

    /**
     * 获取已注册的Docker镜像分类
     *
     * @return
     * @throws Exception
     */
    List<DockerImageCategoryDto> getRegistedCategories() throws Exception;

    /**
     * 查询未注册的Docker镜像分类
     *
     * @return
     * @throws Exception
     */
    List<DockerImageCategoryDto> getUnregistDockerImageCategories() throws Exception;

    /**
     * 注册Docker镜像分类
     *
     * @param registParam
     * @throws Exception
     */
    DockerImageCategoryDto registDockerImageCategory(ImageCategoryRegistParam registParam) throws Exception;

    /**
     * 删除用户Docker镜像分类
     *
     * @param ids
     * @throws Exception
     */
    void deleteUserDockerImageCategories(List<String> ids) throws Exception;

    /**
     * 更新用户Docker镜像分类
     *
     * @param updateParam
     * @throws Exception
     */
    DockerImageCategoryDto updateUserDockerImageCategories(ImageCategoryRegistParam updateParam) throws Exception;

    /**
     * 查询未注册的Docker镜像
     *
     * @return
     * @throws Exception
     */
    List<DockerImageDto> getUnregistDockerImages() throws Exception;

    /**
     * 注册Docker镜像
     *
     * @param registParam
     * @throws Exception
     */
    DockerImageDto registDockerImage(ImageRegistParam registParam) throws Exception;

    /**
     * 根据条件查询镜像模板
     *
     * @param categoryType
     * @param templateName
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    PageDto<DockerImageDto> findDockerTemplate(String categoryType, String templateName, int pageNo, int pageSize) throws Exception;

    /**
     * 删除用户Docker镜像
     *
     * @param ids
     * @throws Exception
     */
    void deleteUserDockerImage(List<String> ids) throws Exception;

    /**
     * 更新用户Docker镜像
     *
     * @param updateParam
     * @throws Exception
     */
    DockerImageDto updateUserDockerImage(ImageRegistParam updateParam) throws Exception;

    /**
     * 获取指定镜像的Tags
     *
     * @param imageId
     * @return
     * @throws Exception
     */
    List<DockerImageDto> getDockerImageTags(String imageId) throws Exception;

    /**
     * 根据镜像分类查询镜像信息
     * @param name
     * @return
     */
    List<DockerImageDto> queryImageByCategory(String name) throws Exception;

    /**
     * 根据镜像分类和镜像名称查询镜像
     * @param category
     * @param name
     * @return
     */
    List<DockerImageDto> queryImageByCategoryAndName(String category, String name) throws Exception;


    /**
     * 查询  镜像 列表
     *  @param  subMethod
     * @param   subParameters
     * @return   List<DockerImageDto>
     */
    List<DockerImageDto>  queryImage(String subMethod , String subParameters)throws Exception;


    /**
     * 查询  镜像分类 列表
     *  @param  subMethod
     * @return   List<DockerImageCategoryDto>
     */
    List<DockerImageCategoryDto> queryCategory(String subMethod) throws Exception;

    /**
     * 查询镜像分类信息列表
     * @param commonQueryParam
     * @return
     */
    PageDto<DockerImageCategoryDto> queryCategoryList(CommonQueryParam commonQueryParam) throws Exception;

    /**
     * 查询镜像信息列表
     * @param commonQueryParam
     * @return
     * @throws Exception
     */
    PageDto<DockerImageDto> queryImageList(CommonQueryParam commonQueryParam) throws Exception;
}
