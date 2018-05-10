package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.NetL2InfoDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 17-4-7.
 */
public interface NetL2InfoDao {
    NetL2InfoDto selectNetL2InfById(String id)  throws DaoException;

    List<NetL2InfoDto> selectNetL2ListByType(String type) throws DaoException;

    void deleteNetL2ById(String id) throws DaoException;

    NetL2InfoDto createNetL2(NetL2InfoDto netL2InfoDto) throws DaoException;

    NetL2InfoDto updateNetL2(NetL2InfoDto netL2InfoDto) throws DaoException;
}
