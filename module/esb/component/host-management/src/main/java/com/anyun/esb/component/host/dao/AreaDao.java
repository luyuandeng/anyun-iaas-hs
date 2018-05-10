package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.AreaDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by gp on 17-3-23.
 */
public interface AreaDao {

    /**
     * 查询所有的区域
     */
    List<AreaDto> queryAllArea();

    /**
     * 根据状态查询区域
     * @param status
     * @return
     */
    List<AreaDto> selectAreaByStatus(String status);

    /**
     * 根据类型查询区域
     * @param type
     * @return
     */
    List<AreaDto> selectAreaByType(String type);


    List<AreaDto> selectAreaByTypeAndStatus(String type, String status);

    void create(AreaDto dto) throws DaoException;

    void update(AreaDto dto) throws DaoException;

    AreaDto queryById(String id) throws DaoException;

    void deleteArea(String id) throws DaoException;

    void changeAreaStatus(AreaDto dto) throws DaoException;
}
