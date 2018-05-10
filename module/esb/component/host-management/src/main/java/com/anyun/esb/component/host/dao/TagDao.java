package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.TagDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by admin on 2017/5/9.
 */
public interface TagDao {

    TagDto tagSelectById(String id) throws DaoException;

    TagDto tagCreate(TagDto tagDto) throws DaoException;

    void tagDelete(String id) throws DaoException;

    TagDto tagUpdate(TagDto tagDto) throws DaoException;

    void tagDeleteByResourceId(String resourceId) throws DaoException;
}
