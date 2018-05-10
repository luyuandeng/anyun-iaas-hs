package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by gaopeng on 16-6-7.
 */
public interface ContainerDao {

    ContainerDto insertContainer(ContainerDto dto) throws DaoException;

    void updateContainer(ContainerDto dto) throws DaoException;

    List<ContainerDto> selectContainerByProject(String id, int type) throws DaoException;

    List<ContainerDto> selectContainerByNetLabel(String label, int type) throws DaoException;

    List<ContainerDto> selectContainerByImage(String image, int type) throws DaoException;

    DockerImageDto selectRegistImageInfoById(String imageId) throws DaoException;

    ContainerDto selectContainerById(String id) throws DaoException;

    ContainerDto selectContainerById(String id, int type);

    List<ContainerDto> selectContainerByPurpose(String project, String purpose, int type);

    List<ContainerDto> selectAllContainerByHost(String host) throws DaoException;

    /**
     * 根据  label 查询容器
     *
     * @return ContainerDto
     * @Param label
     */
    List<ContainerDto> selectContainerByLabel(String label) throws DaoException;

    void deleteContainerById(String id) throws DaoException;

    List<String> selectContianerByHostId(String id);

    ContainerDto selectContainerDtoById(String id) throws DaoException;

    List<ContainerDto> selectContainerByType(int type) throws DaoException;

    List<ContainerDto> batchCreate(ContainerDto containerDto) throws DaoException;

    void changeCalculationScheme(String id, String calculationSchemeId) throws DaoException;

    void changeDiskScheme(String id, String diskSchemeId) throws DaoException;


    /**
     * 根据磁盘方案查询容器列表
     */
    List<ContainerDto> selectContainerListByDiskSchemeId(String diskSchemeId) throws DaoException;


    /**
     * 根据计算方案查询容器列表
     */
    List<ContainerDto> selectContainerListByCalculationSchemeId(String calculationSchemeId) throws DaoException;
}
