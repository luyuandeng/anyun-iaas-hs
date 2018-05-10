package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.DiskSchemeCreateParam;
import com.anyun.exception.DaoException;

import java.util.List;

public interface DiskSchemeDao {



    /**
     * 1、查询磁盘方案详情
     *
     * @param id
     * @retuen DiskSchemeDto
     */
    DiskSchemeDto queryDiskSchemeByid(String id)throws DaoException;




    /**
     * 2、查询计算方案列表
     *
     * @retuen List<CalculationSchemeDto>
     */
    List<DiskSchemeDto> queryDiskSchemeList(String userUniqueId)throws DaoException;




    /**
     * 3,根据条件分页查询磁盘方案列表
     */

    List<DiskSchemeDto> queryDiskSchemeDtoBycondition( CommonQueryParam commonQueryParam)throws DaoException;


    /**
     * 4、删除磁盘方案
     *
     * @param id
     * @retuen Status<String>
     */

    void deleteDiskSchemeById( String id)throws DaoException;

    /**
     * 5、创建磁盘方案
     *
     * @param diskSchemeDto
     * @retuen DiskSchemeDto
     */
    DiskSchemeDto createDiskSchemeDto(DiskSchemeDto diskSchemeDto)throws DaoException;

}
