package com.anyun.esb.component.registry.service.core.impl;

import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ImageCategoryRegistParam;
import com.anyun.cloud.param.ImageRegistParam;
import com.anyun.cloud.param.UserIdsParam;
import com.anyun.esb.component.registry.service.core.RegistryService;

import java.util.List;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class RegistryServiceImpl implements RegistryService {
    private RegistryImageCategoryServiceImpl registryImageCategoryService;
    private RegistryImageServiceImpl registryImageService;

    public RegistryServiceImpl() {
        registryImageCategoryService = new RegistryImageCategoryServiceImpl();
        registryImageService = new RegistryImageServiceImpl();
    }

    @Override
    public List<DockerImageCategoryDto> getRegistedCategories() throws Exception {
        return registryImageCategoryService.getRegistedCategories();
    }

    @Override
    public List<DockerImageCategoryDto> getUnregistDockerImageCategories() throws Exception {
        return registryImageCategoryService.getUnregistDockerImageCategories();
    }

    @Override
    public DockerImageCategoryDto registDockerImageCategory(ImageCategoryRegistParam registParam) throws Exception {
         return registryImageCategoryService.registDockerImageCategory(registParam);
    }

    @Override
    public void deleteUserDockerImageCategories(List<String> ids) throws Exception {
        registryImageCategoryService.deleteUserDockerImageCategories(ids);
    }

    @Override
    public DockerImageCategoryDto updateUserDockerImageCategories(ImageCategoryRegistParam updateParam) throws Exception {
        return registryImageCategoryService.updateUserDockerImageCategories(updateParam);
    }

    @Override
    public List<DockerImageDto> getUnregistDockerImages() throws Exception {
        return registryImageService.getUnregistDockerImages();
    }

    @Override
    public DockerImageDto registDockerImage(ImageRegistParam registParam) throws Exception {
         return registryImageService.registDockerImage(registParam);
    }

    @Override
    public PageDto<DockerImageDto> findDockerTemplate(String categoryType, String templateName, int pageNo, int pageSize) throws Exception {
        return registryImageService.findDockerTemplate(categoryType, templateName, pageNo, pageSize);
    }

    @Override
    public void deleteUserDockerImage(List<String> ids) throws Exception {
        registryImageService.deleteUserDockerImage(ids);
    }

    @Override
    public DockerImageDto updateUserDockerImage(ImageRegistParam updateParam) throws Exception {
        return registryImageService.updateUserDockerImage(updateParam);
    }

    @Override
    public List<DockerImageDto> getDockerImageTags(String imageId) throws Exception {
        return registryImageService.getDockerImageTags(imageId);
    }

    @Override
    public List<DockerImageDto> queryImageByCategory(String name) throws Exception{
        return registryImageService.queryImageByCategory(name);
    }

    @Override
    public List<DockerImageDto> queryImageByCategoryAndName(String category, String name) {
        return registryImageService.queryImageByCategoryAndName(category,name);
    }


    @Override
    public List<DockerImageDto>  queryImage(String subMethod , String subParameters) throws Exception {
        return  registryImageService.queryImage(subMethod,subParameters);
    }


    @Override
    public List<DockerImageCategoryDto> queryCategory(String subMethod)throws Exception{
        return registryImageService.queryCategory(subMethod);
    }

    @Override
    public PageDto<DockerImageCategoryDto> queryCategoryList(CommonQueryParam commonQueryParam) throws Exception {
        return registryImageCategoryService.queryCategoryList(commonQueryParam);
    }

    @Override
    public PageDto<DockerImageDto> queryImageList(CommonQueryParam commonQueryParam) throws Exception {
        return registryImageService.queryImageList(commonQueryParam);
    }


}
