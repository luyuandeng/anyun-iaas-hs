package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.*;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 16-7-22.
 */
public interface NetLabelInfoDao{
    List<NetLabelInfoDto> NetLabelInfoQuery()  throws DaoException;
    void insertContainerNetIpInfo(String container, String label, String ip, String mac)throws DaoException;
    void deleteContainerNetIpInfo(String container, String label) throws DaoException;
    void insertNetLabelInfo(NetLabelInfoDto dto) throws DaoException;
    void deleteNetLabelInfoByLabel(String label) throws  DaoException;
    NetLabelInfoDto selectNetLabelInfoByLabel(String label)  throws DaoException;
    NetLabelInfoDto selectNetLabelInfoByProject(String id)  throws DaoException;
    List<ContainerIpInfoDto> selectContainerIpByCondition(String label, String container) throws DaoException;
    List<NetLabelInfoDto> selectAllBridgeNetLabel() throws DaoException;
    List<NetLabelIpContainerDto> selectContainerLabelIpByProject(String id) throws DaoException;
    NetLabelInfoDto selectNetLabelInfoByLabelAndType(String label, String type) throws DaoException;
    List<ContainerNetIpMacDto> selectContainerNetIpMacByKeyword(String keyword) throws DaoException;
    ContainerIpInfoDto selectContainerIpInfoByIp(String ip) throws DaoException;
    List<NetLabelInfoDto> selectNetLabelInfoByContainer(String container) throws DaoException;
    List<ContainerIpInfoDto> selectContainerIpInfoBySecurityGroup(String label) throws DaoException;
    ContainerIpInfoDto selectContainerIpInfoByCondition(String container, String netLabel) throws DaoException;
    List<IpDto> selectIpByLabel(String label) throws DaoException;
    List<NetLabelInfoDto> selectNetLabelInfoByType(String type) throws DaoException;
    ContainerIpInfoDto selectContainerIpDtoByContainerAndNetLabel(String containerId, String label);
    List<ContainerIpInfoDto> selectContainerIpInfoByProjectIdAndLabel(String projectId, String label) throws DaoException;
}
