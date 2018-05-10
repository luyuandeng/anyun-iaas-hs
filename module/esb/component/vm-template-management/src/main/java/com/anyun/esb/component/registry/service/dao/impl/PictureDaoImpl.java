package com.anyun.esb.component.registry.service.dao.impl;

import com.anyun.cloud.dto.PictureDto;
import com.anyun.esb.component.registry.service.dao.BaseMyBatisDao;
import com.anyun.esb.component.registry.service.dao.PictureDao;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 16-10-18.
 */
public class PictureDaoImpl extends BaseMyBatisDao implements PictureDao {

    @Override
    public List<PictureDto> pictureSelectAll()  throws DaoException {
        String sql="dao.PictureDao.pictureSelectAll";
        return selectList(PictureDto.class,sql,null);
    }
}
