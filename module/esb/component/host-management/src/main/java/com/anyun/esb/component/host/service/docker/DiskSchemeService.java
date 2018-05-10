package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.DiskSchemeCreateParam;

import java.util.List;

public interface DiskSchemeService {

    /**
     * 1、查询磁盘方案详情
     *
     * @param id
     * @retuen DiskSchemeDto
     */

    DiskSchemeDto queryDiskSchemeByid(String id) throws Exception;


    /**
     * 2、查询计算方案列表
     *
     * @retuen List<DiskSchemeDto>
     */

     List<DiskSchemeDto> queryDiskSchemeList(String userUniqueId) throws Exception;


    /**
     * 3,根据条件分页查询磁盘方案列表
     */

    PageDto<DiskSchemeDto> queryDiskSchemeDtoBycondition(CommonQueryParam commonQueryParam) throws Exception;


    /**
     * 4、删除磁盘方案
     *
     * @param id
     * @retuen Status<String>
     */

     void deleteDiskSchemeById( String id) throws Exception;

    /**
     * 5、创建磁盘方案
     *
     * @param diskSchemeCreateParam
     * @retuen DiskSchemeDto
     */
    DiskSchemeDto createDiskSchemeDto(DiskSchemeCreateParam diskSchemeCreateParam) throws Exception;
}
