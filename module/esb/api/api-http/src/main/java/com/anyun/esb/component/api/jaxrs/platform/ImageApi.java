package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;
import org.apache.camel.Produce;

import javax.ws.rs.*;


/**
 * Created by gaopeng on 16-7-6.
 */
@Path("/images")
public class ImageApi {


    /**
     * 1、查询 镜像 列表
     *
     * @param userUniqueId  String  false  用户标识
     * @param subMethod     String  true   子方法名称
     *                      QUERY_UNREGISTRY： 查询未注册镜像
     *                      QUERY_REGISTRY ：查询已经注册镜像
     *                      QUERY_BY_CATEGORY：根据镜像分类名称 查询镜像
     *                      category  String  true   镜像分类名称
     *                      QUERY_BY_NAME_CATEGORY   根据镜像名称和分类名称查询经镜像
     *                      name       String    true  镜像名称
     *                      category   String    true  镜像信息里面的分类字段
     * @param subParameters String  true  子参数   子方法对应的参数
     * @return List<DockerImageDto>
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "image.query",
            service = "image_query")
    public String getImageList(String userUniqueId, String subMethod, String subParameters) {
        return null;
    }


    /**
     * 2、注册  镜像
     *
     * @param body
     */
    @PUT
    @Path("/registry")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "images.registry",
            service = "images_registry")
    public String registryImage(String body) {
        return null;
    }

    /**
     * 3、修改 镜像
     *
     * @param body
     */
    @POST
    @Path("/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "images.update",
            service = "images_update")
    public String updateImage(String body) {
        return null;
    }

    /**
     * 4、 删除 镜像
     *
     * @param
     */
    @DELETE
    @Path("/delete/{ids}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "images.delete.docker",
            service = "images_delete_docker")
    public String deleteImage(@PathParam("ids") String ids, String userUniqueId) {
        return null;
    }

    /**
     * 5、查询  镜像分类 列表
     *
     * @param userUniqueId  String  false  用户标识
     * @param subMethod     String  true   子方法名称
     *                      QUERY_UNREGISTRY： 查询未注册镜像分类
     *                      QUERY_REGISTRY ：查询已经注册镜像分类
     * @return List<DockerImageCategoryDto>
     */
    @GET
    @Path("/category/list")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "category.query",
            service = "category_query")
    public String getCategoryList(String userUniqueId, String subMethod) {
        return null;
    }

    /**
     * 6、注册 镜像分类
     *
     * @param body
     */
    @PUT
    @Path("/category/registry")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "images.registry.category",
            service = "images_registry_category")
    public String registryCategory(String body) {
        return null;
    }

    /**
     * 7、修改镜像分类
     *
     * @param body
     */
    @POST
    @Path("/category/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "images.update.category",
            service = "images_update_category")
    public String updateCategory(String body) {
        return null;
    }


    /**
     * 8、删除  镜像分类
     *
     * @param
     */
    @DELETE
    @Path("/category/delete/{ids}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "images.delete.category",
            service = "images_delete_category")
    public String deleteCategory(@PathParam("ids") String ids, String userUniqueId) {
        return null;
    }

    /**
     * 9、查询镜像分类信息列表 --新增接口
     * @param s
     * @return
     */
    @GET
    @Path("/category/queryCategoryList/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "images.query.category.list",
            service = "images_query_category_list")
    public String queryCategoryList(@PathParam("s" ) String  s){return null;}

    /**
     * 10、查询镜像信息列表  --新增接口
     * @param s
     * @return
     */
    @GET
    @Path("/queryImageList/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "images.query.list",
            service = "images_query_list")
    public String queryImageList(@PathParam("s" ) String  s){return null;}
}



