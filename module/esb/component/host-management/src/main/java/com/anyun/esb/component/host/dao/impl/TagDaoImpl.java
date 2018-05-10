package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.TagDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.TagDao;
import com.anyun.esb.component.host.service.docker.impl.TagServiceImpl;
import com.anyun.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/5/9.
 */
public class TagDaoImpl extends BaseMyBatisDao implements TagDao{
    private static final Logger LOGGER = LoggerFactory.getLogger(TagDaoImpl.class);

    @Override
    public TagDto tagSelectById(String id) throws DaoException {
        String sql = "dao.TagDao.selectTagById";
        return selectOne(TagDto.class, sql, id);
    }

    @Override
    public TagDto tagCreate(TagDto tagDto) throws DaoException {
        String  sql="dao.TagDao.tagInsert";
        LOGGER.debug("參數DTO："+tagDto.asJson());
        tagDto.setCreateDate(new Date());
        tagDto.setLastOpDate(new Date());
        insert(sql,tagDto);
        return tagSelectById(tagDto.getId());
    }

    @Override
    public void tagDelete(String id) throws DaoException {
        String sql = "dao.TagDao.tagDelete";
        delete(sql,id);
    }

    @Override
    public TagDto tagUpdate(TagDto tagDto) throws DaoException {
        String sql = "dao.TagDao.tagUpdate";
        update(sql,tagDto);
        return tagSelectById(tagDto.getId());
    }

    @Override
    public void tagDeleteByResourceId(String resourceId) throws DaoException {
        String sql = "dao.TagDao.tagDeleteByResourceId";
        delete(sql,resourceId);
    }
}
