package com.anyun.esb.component.registry.service.dao;

import com.anyun.cloud.dto.PictureDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 16-10-18.
 */
public interface PictureDao {
    List<PictureDto> pictureSelectAll() throws DaoException;
}
