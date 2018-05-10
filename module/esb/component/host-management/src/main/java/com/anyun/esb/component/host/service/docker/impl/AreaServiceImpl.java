package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.AreaDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.AreaCreateParam;
import com.anyun.cloud.param.AreaUpdateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.common.JdbcUtils;
import com.anyun.esb.component.host.dao.AreaDao;
import com.anyun.esb.component.host.dao.impl.AreaDaoImpl;
import com.anyun.esb.component.host.service.docker.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 17-3-23.
 */
public class AreaServiceImpl implements AreaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AreaServiceImpl.class);
    private AreaDao areaDao = new AreaDaoImpl();

    @Override
    public List<AreaDto> queryAeaByStatus(String status) {
        List<AreaDto> list = areaDao.selectAreaByStatus(status);
        if (list == null) {
            return null;
        }
        return list;
    }

    @Override
    public List<AreaDto> queryAeaBytype(String type) {
        List<AreaDto> list = areaDao.selectAreaByType(type);
        if (list == null) {
            return null;
        }
        return list;
    }

    @Override
    public List<AreaDto> queryAeaBytypeAndStatus(String type, String status) {
        List<AreaDto> list = areaDao.selectAreaByTypeAndStatus(type, status);
        if (list == null) {
            return null;
        }
        return list;
    }

    @Override
    public void createArea(AreaCreateParam param) throws Exception {
        String id = StringUtils.uuidGen();
        AreaDto dto = new AreaDto();
        dto.setId(id);
        dto.setStatus(param.getStatus());
        dto.setType(param.getType());
        dto.setName(param.getName());
        dto.setDescription(param.getDescription());

        areaDao.create(dto);

    }

    @Override
    public void updateArea(AreaUpdateParam param) throws Exception {
        AreaDto dto = new AreaDto();
        dto.setId(param.getId());
        dto.setStatus(param.getStatus());
        dto.setType(param.getType());
        dto.setName(param.getName());
        dto.setDescription(param.getDescription());

        areaDao.update(dto);
    }

    @Override
    public AreaDto queryById(String id) throws Exception {
        AreaDto dto = areaDao.queryById(id);
        return dto;
    }

    @Override
    public void deleteArea(String id) throws Exception {
        areaDao.deleteArea(id);
    }

    @Override
    public void changeAreaStatus(AreaUpdateParam param) throws Exception {
        AreaDto dto = new AreaDto();
        dto.setId(param.getId());
        dto.setStatus(param.getStatus());
        dto.setType(param.getType());
        dto.setName(param.getName());
        dto.setDescription(param.getDescription());
        areaDao.changeAreaStatus(dto);
    }

    @Override
    public PageDto<AreaDto> queryByConditions(CommonQueryParam param) throws Exception {
        PageDto<AreaDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(param.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.area_info_base.* FROM  anyuncloud.area_info_base  left   join   anyuncloud.tag_info   on  anyuncloud.area_info_base.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.area_info_base";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (param.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<AreaDto> list = JdbcUtils.getList(AreaDto.class, sql);
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
            List<AreaDto> list = JdbcUtils.getList(AreaDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<AreaDto> l = JdbcUtils.getList(AreaDto.class, sql);
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
