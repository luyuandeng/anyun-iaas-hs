package com.anyun.esb.component.registry.service.core.impl;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ImageCategoryRegistParam;
import com.anyun.cloud.param.UserIdsParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.registry.common.DockerCommon;
import com.anyun.esb.component.registry.common.JdbcUtils;
import com.anyun.esb.component.registry.service.dao.DockerImageCategoryDao;
import com.anyun.esb.component.registry.service.dao.impl.DockerImageCategoryDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class RegistryImageCategoryServiceImpl extends RegistryServiceAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryImageCategoryServiceImpl.class);

    private  DockerImageCategoryDao dockerImageCategoryDao;
    public  RegistryImageCategoryServiceImpl(){dockerImageCategoryDao = new DockerImageCategoryDaoImpl();}
    @Override
    public List<DockerImageCategoryDto> getRegistedCategories() throws Exception {
        List<DockerImageCategoryDto> CategoryImages = dockerImageCategoryDao.selectRegistryCategories();
        return CategoryImages;
    }

    @Override
    public List<DockerImageCategoryDto> getUnregistDockerImageCategories() throws Exception {

        List<DockerImageCategoryDto> CategoryImages = DockerCommon.getImageCategories();
        for (DockerImageCategoryDto dto :CategoryImages) {
            DockerImageCategoryDto category= dockerImageCategoryDao.selectById(dto.getId());
            if(category==null){
                DockerImageCategoryDto dto1 = new  DockerImageCategoryDto();
                dto1.setId(dto.getId());
                dto1.setStatus(0);
                dto1.setDateCreate(dto.getDateCreate());
                dto1.setDateLastModify(new Date());
                dto1.setDescript(dto.getDescript());
                dto1.setName(dto.getName());
                dto1.setShortName(dto.getShortName());
                dockerImageCategoryDao.insertUnregistDockerImageCategories(dto1);
            }
        }
        List<DockerImageCategoryDto> CategoryDtos =dockerImageCategoryDao.selectUnregistCategories();
        return CategoryDtos;
    }

    @Override
    public DockerImageCategoryDto  registDockerImageCategory(ImageCategoryRegistParam registParam) throws Exception {
       /* DockerImageCategoryDto dto = dockerImageCategoryDao.selectById(registParam.getId());
        if (dto == null){
            DockerImageCategoryDto dockerImageCategoryDto = new DockerImageCategoryDto();
            dockerImageCategoryDto.setId(registParam.getId());
            dockerImageCategoryDto.setName(registParam.getName());
            dockerImageCategoryDto.setShortName(registParam.getShortName());
            dockerImageCategoryDto.setDescript(registParam.getDescript());
            dockerImageCategoryDto.setStatus(1);
            dockerImageCategoryDao.insert(dockerImageCategoryDto);
        }*/
        DockerImageCategoryDto dockerImageCategoryDto = new DockerImageCategoryDto();
        dockerImageCategoryDto.setId(registParam.getId());
        dockerImageCategoryDto.setShortName(registParam.getShortName());
        dockerImageCategoryDto.setDescript(registParam.getDescript());
        dockerImageCategoryDto.setStatus(1);
        return   dockerImageCategoryDao.updateStatus(dockerImageCategoryDto);
    }

    @Override
    public DockerImageCategoryDto updateUserDockerImageCategories(ImageCategoryRegistParam updateParam) throws Exception {
        DockerImageCategoryDto dto = dockerImageCategoryDao.selectById(updateParam.getId());
        if (dto == null)
            throw new Exception("Docker imagesCategory [" + updateParam.getId() + "] not exist");
        DockerImageCategoryDto dockerImageCategoryDto = new DockerImageCategoryDto();
        dockerImageCategoryDto.setId(updateParam.getId());
        dockerImageCategoryDto.setShortName(updateParam.getShortName());
        dockerImageCategoryDto.setDescript(updateParam.getDescript());
        return  dockerImageCategoryDao.update(dockerImageCategoryDto);

    }

    @Override
    public void deleteUserDockerImageCategories(List<String> ids) throws Exception {
        for (String id:ids) {
            ServiceInvoker invoker = new ServiceInvoker<>();
            LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
            invoker.setComponent("anyun-host");
            invoker.setService("tag_delete_resourceId");
            Map<String,String> m=new HashMap<>();
            m.put("resourceId",id);
            invoker.invoke(m,null);
        }
        dockerImageCategoryDao.batchDockerImageCategoriesStatus(ids,0);
    }

    @Override
    public PageDto<DockerImageCategoryDto> queryCategoryList(CommonQueryParam commonQueryParam) throws Exception {
        PageDto<DockerImageCategoryDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(commonQueryParam.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.image_ctegory_docker.* FROM  anyuncloud.image_ctegory_docker  left   join   anyuncloud.tag_info   on  anyuncloud.image_ctegory_docker.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.image_ctegory_docker";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (commonQueryParam.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<DockerImageCategoryDto> list = JdbcUtils.getList(DockerImageCategoryDto.class, sql);
            total = (list == null ? 0 : list.size());
            pageDto.setPageSize(commonQueryParam.getLimit());
            pageDto.setPageNumber(commonQueryParam.getStart());
            pageDto.setTotal(total);
            return pageDto;
        } else { //分页查询
            int start = commonQueryParam.getStart();//查询页码
            int limit = commonQueryParam.getLimit();//每页记录
            boolean replyWithCount = commonQueryParam.isReplyWithCount();//是否返回总条数
            String sortBy = commonQueryParam.getSortBy();//排序字段
            String sortDirection = commonQueryParam.getSortDirection();//排序规则

            // 分页参数校验
            if (start <= 0)
                throw new Exception("start is:{" + start + "}");
            if (limit <= 0)
                throw new Exception("limit is:{" + limit + "}");
            int i = (start - 1) * limit;
            String pagingStatement = "limit" + " " + i + "," + limit;  //分页 语句

            //排序 语句
            String sortingStatement = (sortBy != null && !"".equals(sortBy) ? "order by" + " " + sortBy + " " + sortDirection : "");

            sql = selectStatement + " " + whereStatement + " ";
            //查询全部记录
            List<DockerImageCategoryDto> list = JdbcUtils.getList(DockerImageCategoryDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<DockerImageCategoryDto> l = JdbcUtils.getList(DockerImageCategoryDto.class, sql);
                pageDto.setData(l);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = list.size();
            }
            if (replyWithCount)
                pageDto.setTotal(total);
            return pageDto;
        }
    }


}
