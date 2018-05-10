package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.ComposeDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 16-12-6.
 */
public interface ComposeDao {
    void deleteComposeById(String id) throws DaoException;


    List<ComposeDto> selectComposeByUserUniqueId(String userUniqueId) throws DaoException;

    ComposeDto selectComposeById(String id) throws DaoException;

    void insertCompose(ComposeDto composeDto) throws  DaoException;
}
