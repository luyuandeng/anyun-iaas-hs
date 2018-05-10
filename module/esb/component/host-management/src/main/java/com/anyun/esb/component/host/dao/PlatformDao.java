package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.PlatformDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 16-10-13.
 */
public interface PlatformDao {

    void insertPlatform(PlatformDto platformDto) throws DaoException;

    void updatePlatform(PlatformDto platformDto) throws DaoException;

    void updatePlatformByCondition(int status, String id) throws DaoException;

    void deletePlatform(String id) throws DaoException;

    PlatformDto selectPlatformById(String id) throws DaoException;

    List<PlatformDto> selectPlatformByStatus(int status) throws DaoException;

    List<PlatformDto> selectPlatformAll() throws DaoException;

    void updatePlatformByStatusAndId(int  status,String id)throws DaoException;

}