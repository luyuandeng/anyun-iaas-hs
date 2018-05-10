package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CalculationSchemeCreateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.common.JdbcUtils;
import com.anyun.esb.component.host.dao.CalculationSchemeDao;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.impl.CalculationSchemeDaoImpl;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.service.docker.CalculationSchemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CalculationSchemeServiceImpl implements CalculationSchemeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationSchemeServiceImpl.class);
    private CalculationSchemeDao calculationSchemeDao = new CalculationSchemeDaoImpl();
    private ContainerDao containerDao = new ContainerDaoImpl();

    @Override
    public CalculationSchemeDto queryCalculationSchemeInfo(String id) throws Exception {
        return calculationSchemeDao.queryCalculationSchemeInfo(id);
    }

    @Override
    public List<CalculationSchemeDto> queryCalculationSchemeList(String userUniqueId) throws Exception {
        return calculationSchemeDao.queryCalculationSchemeList(userUniqueId);
    }

    @Override
    public PageDto<CalculationSchemeDto> pageQueryCalculationSchemeList(CommonQueryParam commonQueryParam) throws Exception {
        PageDto<CalculationSchemeDto> pageDto  = new PageDto<>();
        String selectStatement = "";//sql查询语句
        String str = JdbcUtils.ListToString(commonQueryParam.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.calculation_scheme_info.* FROM  anyuncloud.calculation_scheme_info  left   join   anyuncloud.tag_info   on  anyuncloud.calculation_scheme_info.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.calculation_scheme_info";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录

        if (commonQueryParam.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<CalculationSchemeDto> list = JdbcUtils.getList(CalculationSchemeDto.class, sql);
            total = (list == null ? 0 : list.size());
            pageDto.setPageSize(commonQueryParam.getLimit());
            pageDto.setPageNumber(commonQueryParam.getStart());
            pageDto.setTotal(total);
            return pageDto;
        }
        else {
            //分页查询
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
            List<CalculationSchemeDto> list = JdbcUtils.getList(CalculationSchemeDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<CalculationSchemeDto> l = JdbcUtils.getList(CalculationSchemeDto.class, sql);
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
    public void deleteCalculationScheme(String id) throws Exception {
        calculationSchemeDao.deleteCalculationScheme(id);
    }


    @Override
    public CalculationSchemeDto createCalculationScheme(CalculationSchemeCreateParam param) throws Exception {
        String id = StringUtils.uuidGen();
        CalculationSchemeDto calculationSchemeDto = new CalculationSchemeDto();
        calculationSchemeDto.setId(id);
        calculationSchemeDto.setName(param.getName());
        calculationSchemeDto.setCpuShares(param.getCpuShares());
        calculationSchemeDto.setMemory(param.getMemory());
        calculationSchemeDto.setDescription(param.getDescription());
        return calculationSchemeDao.createCalculationScheme(calculationSchemeDto);
    }
}
