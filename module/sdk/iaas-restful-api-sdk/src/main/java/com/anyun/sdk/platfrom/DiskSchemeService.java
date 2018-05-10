package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.DiskSchemeCreateParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by jt on 17-11-23.
 */
public interface DiskSchemeService {

    /**
     * 1、查询磁盘方案详情
     *
     * @param id
     * @retuen DiskSchemeDto
     */

    DiskSchemeDto queryDiskSchemeByid(String id, String userUniqueId) throws RestfulApiException;


    /**
     * 2、查询计算方案列表
     *
     * @retuen List<DiskSchemeDto>
     */

    List<DiskSchemeDto> queryDiskSchemeList(String userUniqueId) throws RestfulApiException;


    /**
     * 3,根据条件分页查询磁盘方案列表
     */

    PageDto<DiskSchemeDto> queryDiskSchemeDtoBycondition(CommonQueryParam commonQueryParam) throws RestfulApiException;


    /**
     * 4、删除磁盘方案
     *
     * @param id
     * @retuen Status<String>
     */

    Status<String> deleteDiskSchemeById(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 5、创建磁盘方案
     *
     * @param diskSchemeCreateParam
     * @retuen DiskSchemeDto
     */
    DiskSchemeDto createDiskSchemeDto(DiskSchemeCreateParam diskSchemeCreateParam) throws RestfulApiException;
}
