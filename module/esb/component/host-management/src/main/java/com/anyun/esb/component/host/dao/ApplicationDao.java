package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.ApplicationInfoDto;
import com.anyun.cloud.dto.ApplicationInfoLoadDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 16-10-25.
 */
public interface ApplicationDao {
    void applicationDeleteById(String id) throws DaoException;

    ApplicationInfoDto applicationSelectById(String id) throws DaoException;

    List<ApplicationInfoDto> applicationSelectByProject(String project) throws DaoException;

    ApplicationInfoDto applicationInsert(ApplicationInfoDto applicationInfoDto) throws DaoException;

    ApplicationInfoLoadDto loadInsert(ApplicationInfoLoadDto applicationInfoLoadDto) throws DaoException;

    List<ApplicationInfoLoadDto> selectLoadDtoByApplication(String id) throws DaoException;

    void deleteLoadByApplication(String id) throws DaoException;

    ApplicationInfoDto selectApplicationDtoByTemplateContainer(String container) throws DaoException;


    /**
     * 根据负载id查询负载信息
     *
     * @param id 负载主键
     * @return ApplicationInfoLoadDto
     */
    ApplicationInfoLoadDto selectLoadDtoById(String id) throws DaoException;

    /**
     * 更新应用表 负载数量
     *
     * @param id  应用主键
     * @param total 负载数量
     */
    void updateLoadsTotal(String id, int total) throws DaoException;


    /**
     * 删除负载
     * @param id  负载id
     */
    void deleteLoad(String id) throws DaoException;
}
