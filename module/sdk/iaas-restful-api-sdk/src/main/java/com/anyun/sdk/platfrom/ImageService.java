package com.anyun.sdk.platfrom;
import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ImageCategoryRegistParam;
import com.anyun.cloud.param.ImageRegistParam;
import com.anyun.cloud.param.UserIdsParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import java.util.List;

/**
 * Created by sxt on 16-7-8.
 */
public interface ImageService {

    /**
     * 查询未注册Docker镜像
     * @return List<DockerImageDto>
     * @throws RestfulApiException
     */
    List<DockerImageDto> queryUnregistryImages(String userUniqueId) throws RestfulApiException;

    /**
     * 查询已注册Docker镜像
     * @return List<DockerImageDto>
     * @throws RestfulApiException
     */
    List<DockerImageDto> queryRegistryImages(String userUniqueId) throws RestfulApiException;

    /**
     * 注册Docker镜像
     * @param  registParam
     * @throws RestfulApiException
     */
    DockerImageDto registryDockerImage(ImageRegistParam registParam) throws RestfulApiException;


    /**
     * 更新用户 Docker镜像
     * @param  updateParam
     * @throws RestfulApiException
     */
    DockerImageDto  updateUserDockerImage(ImageRegistParam updateParam) throws RestfulApiException;

    /**
     * 删除用户Docker镜像
     * @param
     * @throws RestfulApiException
     */
    Status<String> deleteUserDockerImage(List<String> id,String userUniqueId) throws RestfulApiException;

    /**
     * 查询未注册的Docker镜像分类
     * @throws RestfulApiException
     */
    List<DockerImageCategoryDto> queryUnregistryCategories(String userUniqueId) throws RestfulApiException;

    /**
     * 查询已注册的Docker镜像分类
     * @return   DockerImageCategoryDto
     */
    List<DockerImageCategoryDto> queryRegistryCategories(String userUniqueId) throws RestfulApiException;

    /**
     * 注册Docker镜像分类
     * @param registParam
     * @throws RestfulApiException
     */
    DockerImageCategoryDto registDockerImageCategory(ImageCategoryRegistParam registParam) throws RestfulApiException;

    /**
     * 更新用户Docker镜像分类
     * @param updateParam
     * @throws RestfulApiException
     */
    DockerImageCategoryDto updateUserDockerImageCategories(ImageCategoryRegistParam updateParam)throws RestfulApiException;

    /**
     * 删除用户Docker镜像分类
     * @param
     */
    Status<String> deleteUserDockerImageCategories(List<String> id,String userUniqueId) throws RestfulApiException;

    /**
     * 根据镜像分类查询镜像信息
     */
    List<DockerImageDto> queryDockerImageByCategory(String name,String userUniqueId) throws RestfulApiException;

    /**
     * 根据镜像分类和镜像名称查询镜像
     * @param category
     * @param name
     * @return
     * @throws RestfulApiException
     */
    List<DockerImageDto> queryDockerImageByCategoryAndName(String category, String name,String userUniqueId) throws RestfulApiException;

    /**
     * 查询  镜像 列表
     *  @param  subMethod
     * @param   subParameters
     * @return   List<DockerImageDto>
     */
    List<DockerImageDto>  queryImage(String userUniqueId,String subMethod , String subParameters)throws RestfulApiException;


    /**
     * 查询  镜像分类 列表
     *  @param  subMethod
     * @return   List<DockerImageCategoryDto>
     */
    List<DockerImageCategoryDto> queryCategory(String userUniqueId,String subMethod)throws RestfulApiException;


    /**
     * 9、查询镜像分类信息列表
     * @param commonQueryParam
     * @return
     * @throws RestfulApiException
     */
    PageDto<DockerImageCategoryDto> queryCategoryList(CommonQueryParam commonQueryParam) throws RestfulApiException;

    /**
     * 10、查询镜像信息列表
     * @param commonQueryParam
     * @return
     * @throws RestfulApiException
     */
    PageDto<DockerImageDto> queryImageList(CommonQueryParam commonQueryParam) throws RestfulApiException;
}
