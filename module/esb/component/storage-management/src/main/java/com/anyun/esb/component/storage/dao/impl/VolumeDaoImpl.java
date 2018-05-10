package com.anyun.esb.component.storage.dao.impl;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.storage.dao.BaseMyBatisDao;
import com.anyun.esb.component.storage.dao.VolumeDao;
import com.anyun.exception.DaoException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 17-4-27.
 */
public class VolumeDaoImpl  extends BaseMyBatisDao implements VolumeDao{

    @Override
    public VolumeDto insertVolume(VolumeDto volumeDto) throws DaoException {
        String sql = "dao.VolumeDao.insertVolume";
        Date date = new Date();
        volumeDto.setCreateDate(date);
        volumeDto.setLastModifyDate(date);
        insert(sql, volumeDto);
        return selectVolumeById(volumeDto.getId());
    }

    @Override
    public VolumeDto selectVolumeById(String id) throws DaoException {
        String sql = "dao.VolumeDao.selectVolumeById";
        return selectOne(VolumeDto.class, sql, id);
    }

    @Override
    public List<VolumeDto> selectVolumeByContainer(String id) throws DaoException {
        String sql="dao.VolumeDao.selectVolumeByContainer";
        Map<String,Object>  m=new HashMap<>();
        m.put("container",id);
        return selectList(VolumeDto.class,sql,m);
    }

    @Override
    public List<VolumeDto> selectVolumeByProject(String id) throws DaoException {
        String sql="dao.VolumeDao.selectVolumeByProject";
        return selectList(VolumeDto.class,sql,id);
    }


    @Override
    public VolumeDto updateVolume(VolumeDto volumeDto) throws DaoException {
        String sql = "dao.VolumeDao.updateVolume";
        volumeDto.setLastModifyDate(new Date());
        update(sql, volumeDto);
        return selectVolumeById(volumeDto.getId());
    }

    @Override
    public void deleteVolumeById(String id) throws DaoException {
        String sql="dao.VolumeDao.deleteVolumeById";
        delete(sql,id);
    }


    @Override
    public void insertContainerVolume(ContainerVolumeDto containerVolumeDto) throws DaoException {
        String sql = "dao.VolumeDao.insertContainerVolume";
        String id = StringUtils.uuidGen();
        containerVolumeDto.setId(id);
        insert(sql, containerVolumeDto);
    }

    @Override
    public void deleteContainerVolume(ContainerVolumeDto containerVolumeDto) throws DaoException {
        String sql = "dao.VolumeDao.deleteContainerVolume";
        delete(sql, containerVolumeDto);
    }

    @Override
    public String selectManagementIpByContainer(String container) throws DaoException {
        String sql="dao.VolumeDao.selectManagementIpByContainer";
        return selectOne(String.class,sql,container);
    }

    @Override
    public List<String> selectContainerByVolume(String id) throws DaoException {
        String sql="dao.VolumeDao.selectContainerByVolume";
        return selectList(String.class,sql,id);
    }

    @Override
    public ContainerVolumeDto selectContainerMountVolumeInfoByCondition(String container, String volume) throws DaoException {
        String sql="dao.VolumeDao.selectContainerMountVolumeInfoByCondition";
        Map<String,Object> condition=new HashMap<>();
        condition.put("container",container);
        condition.put("volume",volume);
        return selectOne(ContainerVolumeDto.class,sql,condition);
    }

    @Override
    public List<VolumeDto> queryAllVolume(String purpose) throws DaoException {
        String sql = "dao.VolumeDao.selectVolumeByStroagePurpose";
        return selectList(VolumeDto.class,sql,purpose);
    }

    @Override
    public List<VolumeDto> selectAllVolume() throws DaoException {
        String sql = "dao.VolumeDao.selectAllVolume";
        return selectList(VolumeDto.class,sql,null);
    }

    @Override
    public List<ContainerVolumeDto> selectContainerVolumeDtoList(String container, String volume) throws DaoException {
        String sql = "dao.VolumeDao.selectContainerVolumeDtoList";
        Map<String,Object>  params=new HashMap<>();
        params.put("container",container);
        params.put("volume",volume);
        return selectList(ContainerVolumeDto.class,sql,params);
    }

    @Override
    public void deleteContainerVolumeByVolume(String v) throws DaoException {
        String sql="dao.VolumeDao.deleteContainerVolumeByVolume";
        delete(sql,v);
    }

    @Override
    public List<VolumeDto> selectVolumeByStorageId(String storageId) throws DaoException {
        String sql="dao.VolumeDao.selectVolumeByStorageId";
        return  selectList(VolumeDto.class,sql,storageId);
    }
}
