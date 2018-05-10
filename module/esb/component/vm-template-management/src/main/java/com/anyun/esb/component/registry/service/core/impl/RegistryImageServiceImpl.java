package com.anyun.esb.component.registry.service.core.impl;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ImageRegistParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.registry.common.DockerCommon;
import com.anyun.esb.component.registry.common.JdbcUtils;
import com.anyun.esb.component.registry.service.core.RegistryService;
import com.anyun.esb.component.registry.service.dao.DockerImageDao;
import com.anyun.esb.component.registry.service.dao.impl.DockerImageDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class RegistryImageServiceImpl extends RegistryServiceAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryImageServiceImpl.class);
    private DockerImageDao dockerImageDao;

    public RegistryImageServiceImpl() {
        dockerImageDao = new DockerImageDaoImpl();
    }

    @Override
    public List<DockerImageDto> getUnregistDockerImages() throws Exception {
        List<DockerImageDto> unregistDtos = new ArrayList<>();
        List<DockerImageDto> registryImages = null;
        try {
            registryImages = DockerCommon.getImageDtos();
        } catch (Exception e) {
            LOGGER.debug("Exception:[{}]", e);
            return unregistDtos;
        }
        for (DockerImageDto image : registryImages) {
            DockerImageDto dto = dockerImageDao.selectById(image.getId());
            if (dto == null) {
                DockerImageDto dto1 = new DockerImageDto();
                dto1.setId(image.getId());
                dto1.setName(image.getName());
                dto1.setCategory(image.getCategory());
                dto1.setDescript(image.getDescript());
                dto1.setCreateDate(new Date());
                dto1.setTag(image.getTag());
                dto1.setLastModifyDate(new Date());
                dto1.setUserDefineName(image.getUserDefineName());
                dto1.setIcon(image.getIcon());
                dto1.setStatus(0);
                dockerImageDao.insertUnregistDockerImages(dto1);
            }
        }
        unregistDtos = dockerImageDao.selectUnregistDockerImages();
        return unregistDtos;
    }

    @Override
    public DockerImageDto registDockerImage(ImageRegistParam registParam) throws Exception {
       // DockerImageDto dto = dockerImageDao.selectById(registParam.getId());
        /*if (dto != null) {
            if (dto.getStatus() != 0)
                throw new Exception("Image [" + registParam.getId() + "] exist");
            else {
                dockerImageDao.updateStatus(dto.getId(), 1);
            }
        }*/

        /*DockerImageDto dockerImageDto = DockerCommon.getImage(registParam.getId());
        dockerImageDto.setIcon(registParam.getIcon());
        dockerImageDto.setStatus(registParam.getStatus());
        dockerImageDto.setUserDefineName(registParam.getUserDefineName());
        dockerImageDto.setDescript(registParam.getDescript());*/
        //dockerImageDao.insert(dto);
        DockerImageDto dockerImageDto = new DockerImageDto();
        dockerImageDto.setId(registParam.getId());
        dockerImageDto.setIcon(registParam.getIcon());
        dockerImageDto.setStatus(1);
        dockerImageDto.setUserDefineName(registParam.getUserDefineName());
        dockerImageDto.setDescript(registParam.getDescript());
        return  dockerImageDao.updateStatus(dockerImageDto);
    }

    @Override
    public PageDto<DockerImageDto> findDockerTemplate(String categoryType, String templateName, int pageNo, int pageSize) throws Exception {
        return dockerImageDao.selectImageByConditions(categoryType, templateName, pageNo, pageSize);
    }

    @Override
    public void deleteUserDockerImage(List<String> ids) throws Exception {
        List<String> list = new ArrayList<String>();
        for (String id : ids) {
            DockerImageDto dockerImageDto = dockerImageDao.selectById(id);
            if (dockerImageDto != null) {
                list.add(id);
            }
            ServiceInvoker invoker = new ServiceInvoker<>();
            LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
            invoker.setComponent("anyun-host");
            invoker.setService("tag_delete_resourceId");
            Map<String,String> m=new HashMap<>();
            m.put("resourceId",id);
            invoker.invoke(m,null);
          /*  if (dockerImageDto == null)
                ids.remove(id);
            else if (dockerImageDto.getStatus() != 1)
                ids.remove(id);*/
        }

        dockerImageDao.batchUpdateStatus(list, 0);
    }

    @Override
    public DockerImageDto updateUserDockerImage(ImageRegistParam updateParam) throws Exception {
        DockerImageDto dto = dockerImageDao.selectById(updateParam.getId());
        if (dto == null)
            throw new Exception("Image [" + updateParam.getId() + "] exist");
        if (dto.getStatus() == 0)
            throw new Exception("Docker images [" + updateParam.getId() + "] is removed");
        if (dto.getCategory().startsWith("usr/"))
            throw new Exception("Image [" + updateParam.getId() + "] is not user image");
        dto.setDescript(updateParam.getDescript());
        dto.setUserDefineName(updateParam.getUserDefineName());
        dto.setIcon(updateParam.getIcon());
        dto.setStatus(updateParam.getStatus());
        dockerImageDao.update(dto);
        return dto;
    }

    @Override
    public List<DockerImageDto> getDockerImageTags(String imageId) throws Exception {
        DockerImageDto dto = dockerImageDao.selectById(imageId);
        List<DockerImageDto> dtos = dockerImageDao.selectByFullName(dto.getCategory(), dto.getName());
        return dtos;
    }

    @Override
    public List<DockerImageDto> queryImageByCategory(String name) {
        List<DockerImageDto> list = dockerImageDao.selectIamgeByCategory(name);
        return list;
    }

    @Override
    public List<DockerImageDto> queryImageByCategoryAndName(String category, String name) {
        List<DockerImageDto> list = dockerImageDao.selectByFullName(category, name);
        return list;
    }

    @Override
    public List<DockerImageDto> queryImage(String subMethod, String subParameters) throws Exception {
        List<DockerImageDto> l = null;
        switch (subMethod) {
            case "QUERY_UNREGISTRY":
                l = getUnregistDockerImages();
                break;
            case "QUERY_REGISTRY":
                l = dockerImageDao.DockerImageQueryRegistry();
                break;
            case "QUERY_BY_CATEGORY":
                if (StringUtils.isEmpty(subParameters))
                    throw new Exception("subParameters is empty");
                l = queryImageByCategory(subParameters);
                break;
            case "QUERY_BY_NAME_CATEGORY":
                if (StringUtils.isEmpty(subParameters))
                    throw new Exception("subParameters is empty");
                String[] array = subParameters.replace("|", ",").split(",");
                if (array.length != 2)
                    throw new Exception("subParameters format  error");
                if (StringUtils.isEmpty(array[0]))
                    throw new Exception("name is empty");
                if (StringUtils.isEmpty(array[1]))
                    throw new Exception("category is empty");
                String name = array[0];
                String category = array[1];
                l = queryImageByCategoryAndName(category, name);
                break;
            default:
                throw new Exception("subMethod" + subMethod + "  does not exist");
        }
        return l;
    }

    @Override
    public List<DockerImageCategoryDto> queryCategory(String subMethod) throws Exception {
        List<DockerImageCategoryDto> l = null;
        RegistryService service=new RegistryServiceImpl();
        switch (subMethod) {
            case "QUERY_UNREGISTRY":
                l = service.getUnregistDockerImageCategories();
                break;
            case "QUERY_REGISTRY":
                l = service.getRegistedCategories();
                break;
            default:
                throw new Exception("subMethod" + subMethod + "  does not exist");
        }
        return l;
    }

    @Override
    public PageDto<DockerImageDto> queryImageList(CommonQueryParam commonQueryParam) throws Exception {
        PageDto<DockerImageDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(commonQueryParam.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.image_docker.* FROM  anyuncloud.image_docker  left   join   anyuncloud.tag_info   on  anyuncloud.image_docker.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.image_docker";
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
            List<DockerImageDto> list = JdbcUtils.getList(DockerImageDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<DockerImageDto> l = JdbcUtils.getList(DockerImageDto.class, sql);
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
