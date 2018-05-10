package com.anyun.esb.component.storage.service.storage;

import com.anyun.cloud.dto.ContainerVolumeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.param.*;

import java.util.List;

/**
 * Created by gp on 17-4-27.
 */
public interface VolumeService {

    /**
     * 创建卷
     *
     * @param param
     * @return
     */
    VolumeDto volumeCreate(VolumeCreateParam param) throws Exception;

    /**
     * 修改卷
     *
     * @param param
     * @param mark
     * @return
     */
    VolumeDto volumeUpdate(VolumeUpdateParam param, boolean mark) throws Exception;

    /**
     * 根据卷id 删除卷
     *
     * @param id
     * @return
     */
    void volumeDeleteById(String id) throws Exception;

    /**
     * 根据卷id查询卷
     *
     * @param id
     * @return volumeDto
     */
    VolumeDto volumeQueryById(String id) throws Exception;

    /**
     * 根据容器查询卷
     *
     * @param id
     * @return l
     */
    List<VolumeDto> volumeQueryByContainer(String id) throws Exception;

    /**
     * 根据项目查询卷
     *
     * @param id
     * @return l
     */
    List<VolumeDto> volumeQueryByProject(String id) throws Exception;


    /**
     * 容器挂载卷
     *
     * @param param
     * @return
     */
    void containerMountVolume(ContainerMountVolumeParam param) throws Exception;

    /**
     * 容器卸载卷
     *
     * @param param
     * @return
     */
    void containerUninstallVolume(ContainerUninstallVolumeParam param) throws Exception;

    /**
     * 根据项目卸载卷
     *
     * @param id
     * @return
     */
    void volumeDeleteByProject(String id) throws Exception;

    /**
     * 根据容器卸载所有卷
     *
     * @param id
     * @return
     */
    void containerUninstallAllVolumeByContainer(String id) throws Exception;

    /**
     * 根据项目和容器查询卷
     *
     * @param project
     * @param container
     * @return list
     */
    List<VolumeDto> queryVolumeByProjectAndContainer(String project, String container) throws Exception;

    /**
     * 查询卷 列表
     * @param  userUniqueId
     * @param  subMethod
     * @return List<VolumeDto>
     */
    List<VolumeDto> VolumeQuery(String userUniqueId, String subMethod,String subParameters) throws Exception;


    /**
     * 查询卷由存储
     * @param purpose
     * @return
     * @throws Exception
     */
//    List<VolumeDto> queryVolumeByStorage(String purpose) throws Exception;

    /**
     * 查询卷信息列表
     * @param commonQueryParam
     * @return
     * @throws Exception
     */
    PageDto<VolumeDto> queryVolumeList(CommonQueryParam commonQueryParam) throws Exception;

    List<ContainerVolumeDto> getContainerVolumeDtoList(String container, String volume) throws Exception;

    List<VolumeDto> volumeQueryByStorageId(String storageId) throws Exception;
}
