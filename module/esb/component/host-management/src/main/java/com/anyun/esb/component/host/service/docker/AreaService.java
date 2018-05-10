package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.AreaDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.AreaCreateParam;
import com.anyun.cloud.param.AreaUpdateParam;
import com.anyun.cloud.param.CommonQueryParam;

import java.util.List;

/**
 * Created by gp on 17-3-23.
 */
public interface AreaService {

    /**
     * 根据状态查询区域列表
     * @param status
     * @return
     */
    List<AreaDto> queryAeaByStatus(String status);

    List<AreaDto> queryAeaBytype(String type);

    List<AreaDto> queryAeaBytypeAndStatus(String type, String status);

    /**
     * 创建区域
     * @param param
     * @throws Exception
     */
    void createArea(AreaCreateParam param) throws Exception;

    /**
     * 更新区域
     * @param param
     * @throws Exception
     */
    void updateArea(AreaUpdateParam param) throws Exception;

    /**
     * 根据ID查询区域详情
     * @param id
     * @return
     * @throws Exception
     */
    AreaDto queryById(String id) throws Exception;

    /**
     * 删除区域
     * @param id
     * @throws Exception
     */
    void deleteArea(String id) throws Exception;

    /**
     * 修改区域状态
     * @param param
     * @throws Exception
     */
    void changeAreaStatus(AreaUpdateParam param) throws Exception;

    /**
     * 查询区域状态
     * @param param
     * @return PageDto<AreaDto>
     * @throws Exception
     */
    PageDto<AreaDto> queryByConditions(CommonQueryParam param) throws Exception;
}
