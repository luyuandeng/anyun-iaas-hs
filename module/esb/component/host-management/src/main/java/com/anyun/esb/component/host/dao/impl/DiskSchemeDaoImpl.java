package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.DiskSchemeCreateParam;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.DiskSchemeDao;
import com.anyun.exception.DaoException;

import java.util.List;

public class DiskSchemeDaoImpl extends BaseMyBatisDao implements DiskSchemeDao {

    @Override
    public DiskSchemeDto queryDiskSchemeByid(String id) throws DaoException {

        String sql = "dao.DiskSchemeDao.selectDiskSchemeById";
        return selectOne(DiskSchemeDto.class, sql, id);
    }

    @Override
    public List<DiskSchemeDto> queryDiskSchemeList(String userUniqueId) throws DaoException{
        String sql = "dao.DiskSchemeDao.selectCalculationSchemeAll";
        return selectList(DiskSchemeDto.class,sql,userUniqueId);
    }

    @Override
    public List<DiskSchemeDto> queryDiskSchemeDtoBycondition(CommonQueryParam commonQueryParam) throws DaoException{
        return null;
    }

    @Override
    public void deleteDiskSchemeById(String id) throws DaoException{
        String sql = "dao.DiskSchemeDao.deleteDiskSchemeById";
        delete(sql, id);

    }

    @Override
    public DiskSchemeDto createDiskSchemeDto(DiskSchemeDto diskSchemeDto) throws DaoException{
        String sql = "dao.DiskSchemeDao.createDiskScheme";
        insert(sql, diskSchemeDto);
        String id=diskSchemeDto.getId();
        return queryDiskSchemeByid(id);
    }
}
