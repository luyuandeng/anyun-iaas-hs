package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ImageCategoryRegistParam;
import com.anyun.cloud.param.ImageRegistParam;
import com.anyun.sdk.platfrom.ImageService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-7-8.
 */
public class ImageServiceImpl implements ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    public static final String PATH_QUERY_UNREGISTRY_IMAGES = "/images/queryUnregistryImages";
    public static final String PATH_QUERY_REGISIPY_IMAGES = "/images/queryRegistryImages";
    public static final String PATH_QUERY_UNREGISTRYCATEGORY = "/images/queryUnregistryCategories";
    public static final String PATH_QUERY_REGISTRYCATEGORIES = "/images/queryRegistryCategories";
    public static final String PATH_QUERY_IMAGE_BYCATEGORY = "/images/queryImagesByCategory";
    public static final String PATH_QUERY_IMAGE_BYCATEGORYANDNAME = "/images/queryDockerImageByCategoryAndName";
    public static final String PATH_IMAGES_REGISTRY = "/images/registry";
    public static final String PATH_IMAGES_UPDATE = "/images/update";
    public static final String PATH_IMAGES_DELETE = "/images/delete";
    public static final String PATH_IMAGES_CATEGORY_REGISTRY = "/images/category/registry";
    public static final String PATH_IMAGES_CATEGORY_UPDATE = "/images/category/update";
    public static final String PATH_IMAGES_CATEGORY_DELETE = "/images/category/delete";
    public static final String PATH_IMAGES_LIST = "/images/list";
    public static final String PATH_IMAGES_CATEGORY_LIST = "/images/category/list";
    public static final String PATH_QUERY_IMAGES_LIST = "/images/queryImageList";
    public static final String PATH_QUERY_IMAGES_CATEGORY_LIST = "/images/category/queryCategoryList";

    @Override
    public List<DockerImageDto> queryUnregistryImages(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_UNREGISTRY_IMAGES, params);
        List<DockerImageDto> requests = ResourceClient.convertToListObject(DockerImageDto.class, response);
        return requests;
    }

    @Override
    public List<DockerImageDto> queryRegistryImages(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_REGISIPY_IMAGES, params);
        List<DockerImageDto> requests = ResourceClient.convertToListObject(DockerImageDto.class, response);
        return requests;
    }

    @Override
    public DockerImageDto registryDockerImage(ImageRegistParam registParam) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_IMAGES_REGISTRY, registParam.asJson());
        DockerImageDto requests = ResourceClient.convertToAnyunEntity(DockerImageDto.class, response);
        return requests;

    }

    @Override
    public DockerImageDto updateUserDockerImage(ImageRegistParam updateParam) throws RestfulApiException {
        LOGGER.debug("start update");
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_IMAGES_UPDATE, updateParam.asJson());
        DockerImageDto requests = ResourceClient.convertToAnyunEntity(DockerImageDto.class, response);
        return requests;
    }

    @Override
    public Status<String> deleteUserDockerImage(List<String> id, String userUniqueId) throws RestfulApiException {
        LOGGER.debug("start delete");
        String ids = "";

        for (String i : id) {
            if (i != null && !i.equals("")) {
                ids = ids + i + ",";
            }
        }
        String idss = ids.substring(0, ids.length() - 1).trim();
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        LOGGER.debug(params.toString());
        String response = rsClient.delete(PATH_IMAGES_DELETE + "/" + idss, params);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public List<DockerImageCategoryDto> queryUnregistryCategories(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_UNREGISTRYCATEGORY, params);
        List<DockerImageCategoryDto> requests = ResourceClient.convertToListObject(DockerImageCategoryDto.class, response);
        return requests;
    }

    @Override
    public List<DockerImageCategoryDto> queryRegistryCategories(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_REGISTRYCATEGORIES, params);
        List<DockerImageCategoryDto> requests = ResourceClient.convertToListObject(DockerImageCategoryDto.class, response);
        return requests;
    }

    @Override
    public DockerImageCategoryDto registDockerImageCategory(ImageCategoryRegistParam registParam) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_IMAGES_CATEGORY_REGISTRY, registParam.asJson());
        DockerImageCategoryDto requests = ResourceClient.convertToAnyunEntity(DockerImageCategoryDto.class, response);
        return requests;
    }

    @Override
    public DockerImageCategoryDto updateUserDockerImageCategories(ImageCategoryRegistParam updateParam) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_IMAGES_CATEGORY_UPDATE, updateParam.asJson());
        DockerImageCategoryDto requests = ResourceClient.convertToAnyunEntity(DockerImageCategoryDto.class, response);
        return requests;

    }

    @Override
    public Status<String> deleteUserDockerImageCategories(List<String> id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String ids = "";

        for (String i : id) {
            if (i != null && !i.equals("")) {
                ids = ids + i + ",";
            }
        }
        String idss = ids.substring(0, ids.length() - 1).trim();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.delete(PATH_IMAGES_CATEGORY_DELETE + "/" + idss, params);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public List<DockerImageDto> queryDockerImageByCategory(String name, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_IMAGE_BYCATEGORY, params);
        List<DockerImageDto> requests = ResourceClient.convertToListObject(DockerImageDto.class, response);
        return requests;
    }

    @Override
    public List<DockerImageDto> queryDockerImageByCategoryAndName(String category, String name, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("name", name);
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_IMAGE_BYCATEGORYANDNAME, params);
        List<DockerImageDto> requests = ResourceClient.convertToListObject(DockerImageDto.class, response);
        return requests;
    }

    @Override
    public List<DockerImageDto> queryImage(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("subMethod", subMethod);
        param.put("subParameters", subParameters);
        String response = rsClient.query(PATH_IMAGES_LIST, param);
        List<DockerImageDto> requests = ResourceClient.convertToListObject(DockerImageDto.class, response);
        return requests;
    }

    @Override
    public List<DockerImageCategoryDto> queryCategory(String userUniqueId, String subMethod) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("subMethod", subMethod);
        String response = rsClient.query(PATH_IMAGES_CATEGORY_LIST, param);
        List<DockerImageCategoryDto> requests = ResourceClient.convertToListObject(DockerImageCategoryDto.class, response);
        return requests;
    }

    @Override
    public PageDto<DockerImageCategoryDto> queryCategoryList(CommonQueryParam commonQueryParam) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(commonQueryParam.asJson());
        String response = rsClient.query(PATH_QUERY_IMAGES_CATEGORY_LIST+"/"+commonQueryParam.asJson(), new HashMap<>());
        PageDto<DockerImageCategoryDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public PageDto<DockerImageDto> queryImageList(CommonQueryParam commonQueryParam) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(commonQueryParam.asJson());
        String response = rsClient.query(PATH_QUERY_IMAGES_LIST+"/"+commonQueryParam.asJson(), new HashMap<>());
        PageDto<DockerImageDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }
}
