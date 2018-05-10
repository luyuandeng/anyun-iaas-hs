package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.CalculationSchemeDao;

import java.util.List;

public class CalculationSchemeDaoImpl extends BaseMyBatisDao implements CalculationSchemeDao {
    @Override
    public CalculationSchemeDto queryCalculationSchemeInfo(String id) {
        String sql = "dao.CalculationSchemeDao.selectCalculationSchemeById";
        return selectOne(CalculationSchemeDto.class, sql, id);
    }

    @Override
    public List<CalculationSchemeDto> queryCalculationSchemeList(String userUniqueId) {
        String sql = "dao.CalculationSchemeDao.queryList";
        List<CalculationSchemeDto> list = selectList(CalculationSchemeDto.class,sql,userUniqueId);
        return list;
    }

    @Override
    public void deleteCalculationScheme(String id) {
        String sql = "dao.CalculationSchemeDao.deleteCalculationScheme";
        delete(sql,id);
    }

    @Override
    public CalculationSchemeDto createCalculationScheme(CalculationSchemeDto calculationSchemeDto) {
        String sql = "dao.CalculationSchemeDao.createCalculationScheme";
        insert(sql,calculationSchemeDto);
        return queryCalculationSchemeInfo(calculationSchemeDto.getId());
    }
}
