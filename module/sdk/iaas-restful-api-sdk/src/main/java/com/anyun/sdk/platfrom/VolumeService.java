package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerVolumeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.param.*;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by gp on 17-5-4.
 */
public interface VolumeService {
    /**
     * 创建卷
     *
     * @param param
     * @return
     * @throws RestfulApiException
     */
    VolumeDto volumeCreate(VolumeCreateParam param) throws RestfulApiException;

    /**
     * 修改卷
     *
     * @param param
     * @return
     * @throws RestfulApiException
     */
    VolumeDto volumeUpdate(VolumeUpdateParam param) throws RestfulApiException;

    /**
     * 删除卷
     *
     * @param id
     * @param userUniqueId
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> volumeDeleteById(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 查询卷由Id
     *
     * @param id
     * @param userUniqueId
     * @return VolumeDto
     * @throws RestfulApiException
     */
    VolumeDto volumeQueryById(String id, String userUniqueId) throws RestfulApiException;



    /**
     * 容器挂载卷
     *
     * @param param
     * @return
     * @throws RestfulApiException
     */
    Status<String> containerMountVolume(ContainerMountVolumeParam param) throws RestfulApiException;

    /**
     * 容器卸载卷
     *
     * @param param
     * @return
     * @throws RestfulApiException
     */
    Status<String> containerUninstallVolume(ContainerUninstallVolumeParam param) throws RestfulApiException;

    /**
     * 查询 卷列表
     *
     * @param userUniqueId
     * @param subMethod
     * @return List<VolumeDto>
     * @throws RestfulApiException
     */
    List<VolumeDto> volumeQuery(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException;

    /**
     * 查询卷由存储
     * @param id
     * @param userUniqueId
     * @return
     * @throws RestfulApiException
     */
    List<VolumeDto> queryVolumeByStroage(String id, String userUniqueId) throws RestfulApiException;


    /**
     * 9、查询卷信息列表
     * @param commonQueryParam
     * @return
     * @throws RestfulApiException
     */
    PageDto<VolumeDto> queryVolumeList(CommonQueryParam commonQueryParam) throws RestfulApiException;

    /**
     * 10、根据容器Id或者卷Id查询 容器挂载卷信息
     * @param    container  String  false  容器Id
     * @param    volume     String  false  卷Id
     * @param    userUniqueId   String     用户唯一标示
     * @return   List<ContainerVolumeDto>
     */
    List<ContainerVolumeDto>  getContainerMountVolumeInfo(String  container,String  volume,String userUniqueId) throws  RestfulApiException;
}
