package com.anyun.esb.component.registry.service.core.impl;

import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.*;
import com.anyun.esb.component.registry.service.core.RegistryService;

import java.util.List;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public abstract class RegistryServiceAdapter implements RegistryService {
    @Override
    public List<DockerImageCategoryDto> getRegistedCategories() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DockerImageCategoryDto> getUnregistDockerImageCategories() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public DockerImageCategoryDto registDockerImageCategory(ImageCategoryRegistParam registParam) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteUserDockerImageCategories(List<String> ids) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public DockerImageCategoryDto updateUserDockerImageCategories(ImageCategoryRegistParam updateParam) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DockerImageDto> getUnregistDockerImages() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public DockerImageDto registDockerImage(ImageRegistParam registParam) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public PageDto<DockerImageDto> findDockerTemplate(String categoryType, String templateName, int pageNo, int pageSize) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteUserDockerImage(List<String> ids) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public DockerImageDto updateUserDockerImage(ImageRegistParam updateParam) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DockerImageDto> getDockerImageTags(String imageId) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DockerImageDto> queryImageByCategory(String name) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DockerImageDto> queryImageByCategoryAndName(String category,String name) throws Exception{
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DockerImageDto>  queryImage(String subMethod , String subParameters)throws Exception{
        throw new UnsupportedOperationException();

    }

    @Override
    public List<DockerImageCategoryDto> queryCategory(String subMethod)throws Exception{
        throw new UnsupportedOperationException();
    }

    @Override
    public  PageDto<DockerImageCategoryDto> queryCategoryList(CommonQueryParam commonQueryParam) throws Exception{
        throw new UnsupportedOperationException();
    }

    @Override
    public  PageDto<DockerImageDto> queryImageList(CommonQueryParam commonQueryParam) throws Exception{
        throw new UnsupportedOperationException();
    }
}
