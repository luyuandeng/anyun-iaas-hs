package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.TagDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.TagCreateParam;
import com.anyun.cloud.param.TagUpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.common.JdbcUtils;
import com.anyun.esb.component.host.dao.TagDao;
import com.anyun.esb.component.host.dao.impl.TagDaoImpl;
import com.anyun.esb.component.host.service.docker.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by admin on 2017/5/9.
 */
public class TagServiceImpl implements TagService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagServiceImpl.class);
    private TagDao tagDao = new TagDaoImpl();


    @Override
    public PageDto<TagDto> queryByConditions(CommonQueryParam param) throws Exception {
        PageDto<TagDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "SELECT * FROM anyuncloud.tag_info";//select 查询语句
        String str = JdbcUtils.ListToString(param.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
//        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
//            selectStatement = "SELECT anyuncloud.area_info_base.* FROM  anyuncloud.area_info_base  left   join   anyuncloud.tag_info   on  anyuncloud.area_info_base.id  =  anyuncloud.tag_info.resourceId";
//        } else {
//            selectStatement = "SELECT * FROM anyuncloud.area_info_base";
//        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (param.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<TagDto> list = JdbcUtils.getList(TagDto.class, sql);
            total = (list == null ? 0 : list.size());
            pageDto.setPageSize(param.getLimit());
            pageDto.setPageNumber(param.getStart());
            pageDto.setTotal(total);
            return pageDto;
        } else { //分页查询
            int start = param.getStart();//查询页码
            int limit = param.getLimit();//每页记录
            boolean replyWithCount = param.isReplyWithCount();//是否返回总条数
            String sortBy = param.getSortBy();//排序字段
            String sortDirection = param.getSortDirection();//排序规则

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
            List<TagDto> list = JdbcUtils.getList(TagDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<TagDto> l = JdbcUtils.getList(TagDto.class, sql);
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

    @Override
    public TagDto createTag(TagCreateParam param) throws Exception {
        String id = StringUtils.uuidGen();
        TagDto dto = new TagDto();
        dto.setId(id);
        dto.setResourceId(param.getResourceId());
        dto.setResourceType(param.getResourceType());
        dto.set__userTag__(param.get__userTag__());
        return tagDao.tagCreate(dto);
    }

    @Override
    public void tagDeleteById(String id) throws Exception {
        tagDao.tagDelete(id);
    }

    @Override
    public TagDto tagUpdate(TagUpdateParam param) throws Exception {
        TagDto dto = new TagDto();
        dto.setId(param.getId());
        dto.set__userTag__(param.get__userTag__());
        dto.setResourceId(param.getResourceId());
        dto.setResourceType(param.getResourceType());
        return tagDao.tagUpdate(dto);
    }

    @Override
    public void tagDeleteByResourceId(String resourceId) throws Exception {
        tagDao.tagDeleteByResourceId(resourceId);
    }

    @Override
    public TagDto queryById(String id) throws Exception {
        TagDto dto = tagDao.tagSelectById(id);
        return dto;
    }
}
