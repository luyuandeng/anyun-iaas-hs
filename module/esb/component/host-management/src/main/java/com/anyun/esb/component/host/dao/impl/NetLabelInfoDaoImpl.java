package com.anyun.esb.component.host.dao.impl;
import com.anyun.cloud.dto.*;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.NetLabelInfoDao;
import com.anyun.exception.DaoException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-7-22.
 */
public class NetLabelInfoDaoImpl extends BaseMyBatisDao implements NetLabelInfoDao {
    @Override
    public List<NetLabelInfoDto> NetLabelInfoQuery( ) throws DaoException {
        String sql = "dao.NetLabelInfoDao.NetLabelInfoQuery";
        return selectList(NetLabelInfoDto.class,sql,"");
    }


    @Override
    public void insertContainerNetIpInfo(String container, String label, String ip,String mac) throws DaoException {
        String sql = "dao.NetLabelInfoDao.insertContainerNetIpInfo";
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("container", container);
        condition.put("label", label);
        condition.put("ip", ip);
        condition.put("mac",mac);
        condition.put("id", StringUtils.uuidGen());
        insert(sql, condition);
    }

    @Override
    public void deleteContainerNetIpInfo(String container, String label) throws DaoException {
        String sql = "dao.NetLabelInfoDao.deleteContainerNetIpInfo";
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("container",container);
        condition.put("label", label);
        delete(sql, condition);
    }

    @Override
    public void insertNetLabelInfo(NetLabelInfoDto dto) throws DaoException{
        dto.setCreatetime(new Date());
        dto.setType("openvswitch");
        String  sql = "dao.NetLabelInfoDao.insertNetLabelInfo";
        insert(sql,dto);
    }

    @Override
    public void deleteNetLabelInfoByLabel(String label) throws DaoException {
        String sql="dao.NetLabelInfoDao.deleteNetLabelInfoByLabel";
        delete(sql,label);
    }

    @Override
    public NetLabelInfoDto selectNetLabelInfoByLabel(String label) throws DaoException {
        String sql  ="dao.NetLabelInfoDao.selectNetLabelInfoByLabel";
        return   selectOne(NetLabelInfoDto.class,sql,label);
    }

    @Override
    public NetLabelInfoDto selectNetLabelInfoByProject(String id) throws DaoException {
        String  sql="dao.NetLabelInfoDao.selectNetLabelInfoByProject";
        return  selectOne(NetLabelInfoDto.class,sql,id);
    }

    @Override
    public List<ContainerIpInfoDto> selectContainerIpByCondition(String label, String container) {
        String sql = "dao.NetLabelInfoDao.selectContainerIpByCondition";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("label",label);
        map.put("container",container);
        return selectList(ContainerIpInfoDto.class,sql,map);
    }

    @Override
    public List<NetLabelInfoDto> selectAllBridgeNetLabel() throws DaoException {
        String sql="dao.NetLabelInfoDao.selectAllBridgeNetLabel";
        return selectList(NetLabelInfoDto.class,sql,null);
    }

    @Override
    public List<NetLabelIpContainerDto> selectContainerLabelIpByProject(String id) throws DaoException {
        String sql="dao.NetLabelInfoDao.selectContainerLabelIpByProject";
        return selectList(NetLabelIpContainerDto.class,sql,id);
    }

    @Override
    public NetLabelInfoDto selectNetLabelInfoByLabelAndType(String label, String type) throws DaoException {
        String sql="dao.NetLabelInfoDao.selectNetLabelInfoByLabelAndType";
        Map<String,Object>   condition  =new HashMap<>();
        condition.put("label",label);
        condition.put("type",type);
        return selectOne(NetLabelInfoDto.class,sql,condition);
    }

    @Override
    public List<ContainerNetIpMacDto> selectContainerNetIpMacByKeyword(String keyword) throws DaoException {
        Map<String,Object>  m=new HashMap<>();
        m.put("keyword",keyword);
        String sql="dao.NetLabelInfoDao.selectContainerNetIpMacByKeyword";
        return selectList(ContainerNetIpMacDto.class,sql,m);
    }

    @Override
    public ContainerIpInfoDto selectContainerIpInfoByIp(String ip) throws DaoException {
        String sql="dao.NetLabelInfoDao.selectContainerIpInfoByIp";
        return selectOne(ContainerIpInfoDto.class,sql,ip);
    }

    @Override
    public List<NetLabelInfoDto> selectNetLabelInfoByContainer(String container) throws DaoException {
        String sql="dao.NetLabelInfoDao.selectNetLabelInfoByContainer";
        Map<String,Object>  m= new HashMap<>();
        m.put("container",container);
        return selectList(NetLabelInfoDto.class,sql,m);
    }

    @Override
    public List<ContainerIpInfoDto> selectContainerIpInfoBySecurityGroup(String label) throws DaoException {
        String  sql="dao.NetLabelInfoDao.selectContainerIpInfoBySecurityGroup";
        Map<String,Object>  m=new HashMap<>();
        m.put("label",label);
        return selectList(ContainerIpInfoDto.class,sql,m);
    }

    @Override
    public ContainerIpInfoDto selectContainerIpInfoByCondition(String container, String netLabel) throws DaoException {
        String sql="dao.NetLabelInfoDao.selectContainerIpInfoByCondition";
        Map<String,Object>  params =new HashMap<String,Object>();
        params.put("container",container);
        params.put("label",netLabel);
        return selectOne(ContainerIpInfoDto.class,sql,params);
    }

    @Override
    public List<IpDto> selectIpByLabel(String label) throws DaoException {
        String sql = "dao.NetLabelInfoDao.selectIpByLabel";
        return selectList(IpDto.class,sql,label);
    }

    @Override
    public List<NetLabelInfoDto> selectNetLabelInfoByType(String type) throws DaoException {
        String sql="dao.NetLabelInfoDao.selectNetLabelInfoByType";
        return selectList(NetLabelInfoDto.class,sql,type);
    }

    @Override
    public ContainerIpInfoDto selectContainerIpDtoByContainerAndNetLabel(String containerId, String label) {
        String sql="dao.NetLabelInfoDao.selectContainerIpDtoByContainerAndNetLabel";
        Map<String,Object> params=new HashMap<>();
        params.put("container",containerId);
        params.put("label",label);
        return selectOne(ContainerIpInfoDto.class,sql,params);
    }

    @Override
    public List<ContainerIpInfoDto> selectContainerIpInfoByProjectIdAndLabel(String projectId, String label) throws DaoException {
        String sql="dao.NetLabelInfoDao.selectContainerIpInFoByProjectAndLabel";
        Map<String,Object>  params=new HashMap<>();
        params.put("projectId",projectId);
        params.put("label",label);
        return selectList(ContainerIpInfoDto.class,sql,params);
    }
}
