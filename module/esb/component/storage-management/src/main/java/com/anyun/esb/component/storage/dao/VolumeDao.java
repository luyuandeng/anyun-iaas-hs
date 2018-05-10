package com.anyun.esb.component.storage.dao;

import com.anyun.cloud.dto.*;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by gp on 17-4-27.
 */
public interface VolumeDao {

    VolumeDto insertVolume(VolumeDto volumeDto) throws DaoException;

    VolumeDto selectVolumeById(String id) throws DaoException;

    List<VolumeDto> selectVolumeByContainer(String id) throws DaoException;

    List<VolumeDto> selectVolumeByProject(String id) throws DaoException;

    VolumeDto updateVolume(VolumeDto volumeDto) throws DaoException;

    void deleteVolumeById(String id) throws DaoException;

    void insertContainerVolume(ContainerVolumeDto containerVolumeDto) throws DaoException;

    void deleteContainerVolume(ContainerVolumeDto containerVolumeDto) throws DaoException;

    String selectManagementIpByContainer(String container) throws DaoException;

    List<String> selectContainerByVolume(String id) throws DaoException;


    ContainerVolumeDto selectContainerMountVolumeInfoByCondition(String container, String volume) throws DaoException;

    List<VolumeDto> queryAllVolume(String purpose) throws DaoException;

    List<VolumeDto> selectAllVolume() throws DaoException;

    List<ContainerVolumeDto> selectContainerVolumeDtoList(String container, String volume) throws DaoException;

    void deleteContainerVolumeByVolume(String v) throws DaoException;

    List<VolumeDto> selectVolumeByStorageId(String storageId) throws DaoException;
}
