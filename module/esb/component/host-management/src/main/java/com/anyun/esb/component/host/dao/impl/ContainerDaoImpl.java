package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.ContainerDto;

import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.exception.DaoException;;
import java.util.*;


/**
 * Created by gaopeng on 16-6-7.
 */
public class ContainerDaoImpl  extends BaseMyBatisDao implements ContainerDao {
    @Override
    public ContainerDto insertContainer(ContainerDto dto) throws DaoException {
        String sql = "dao.ContainerDao.insertContainer";
        insert(sql,dto);
        return  selectContainerDtoById(dto.getId());
    }

    @Override
    public void updateContainer(ContainerDto dto) throws DaoException {
        String sql ="dao.ContainerDao.updateContainer";
        update(sql,dto);
    }

    @Override
    public List<ContainerDto> selectContainerByProject(String project, int type) throws DaoException {
        String   sql ="dao.ContainerDao.selectContainerByProject";
        Map<String,Object>  params=new HashMap<>();
        params.put("project",project);
        params.put("type",type);
        return selectList(ContainerDto.class,sql,params);
    }

    @Override
    public List<ContainerDto> selectContainerByNetLabel(String label, int type) throws DaoException {
        String  sql="dao.ContainerDao.selectContainerByNetLabel";
        Map<String,Object>  params=new HashMap<>();
        params.put("label",label);
        params.put("type",type);
        return  selectList(ContainerDto.class,sql,params);
    }

    @Override
    public List<ContainerDto> selectContainerByImage(String image, int type) throws DaoException {
        String  sql ="dao.ContainerDao.selectContainerByImage";
        Map<String,Object>  params=new HashMap<>();
        params.put("image",image);
        params.put("type",type);
        return    selectList(ContainerDto.class,sql,params);
    }

    @Override
    public DockerImageDto selectRegistImageInfoById(String id) throws DaoException {
        String sql="dao.ContainerDao.selectRegistImageInfoById";
        return   selectOne(DockerImageDto.class,sql,id);
    }

    @Override
    public ContainerDto selectContainerById(String id) throws DaoException {
        String  sql ="dao.ContainerDao";
        return selectById(ContainerDto.class,sql,id);
    }

    @Override
    public ContainerDto selectContainerById(String id, int type) {
        ContainerDto  containerDto=selectContainerById(id);
        if(containerDto!=null){
            if(containerDto.getType()==type)
                return containerDto;
        }
        return   null;
    }

    @Override
    public List<ContainerDto> selectContainerByPurpose(String project, String purpose, int type) {
        String sql = "dao.ContainerDao.selectContainerByPurpose";
        Map<String,Object> params = new HashMap<>();
        params.put("project",project);
        params.put("purpose",purpose);
        params.put("type",type);
        return selectList(ContainerDto.class,sql,params);
    }

    @Override
    public List<ContainerDto> selectAllContainerByHost(String host) throws DaoException {
        String sql = "dao.ContainerDao.selectContainerByHost";
        return selectList(ContainerDto.class,sql,host);
    }

    @Override
    public List<ContainerDto> selectContainerByLabel(String label) throws DaoException {
        String  sql="dao.ContainerDao.selectContainerByLabel";
        return  selectList(ContainerDto.class,sql,label);
    }

    @Override
    public void deleteContainerById(String id) throws DaoException {
        String  sql="dao.ContainerDao.deleteContainerById";
        delete(sql,id);
    }

    @Override
    public List<String> selectContianerByHostId(String id) {
        String sql="dao.ContainerDao.selectContianerByHostId";
        return selectList(String.class,sql,id);
    }

    @Override
    public ContainerDto selectContainerDtoById(String id) throws DaoException {
        String sql="dao.ContainerDao.selectContainerDtoById";
        return selectOne(ContainerDto.class,sql,id);
    }

    @Override
    public List<ContainerDto> selectContainerByType(int type) throws DaoException {
        String sql="dao.ContainerDao.selectContainerByType";
        Map<String,Object>  m=new HashMap<>();
        m.put("type",type);
        return selectList(ContainerDto.class,sql,m);
    }

    @Override
    public List<ContainerDto> batchCreate(ContainerDto containerDto) throws DaoException {
        String sql = "dao.ContainerDao.insertContainer";
        insert(sql,containerDto);
        ContainerDto dto = selectContainerDtoById(containerDto.getId());
        List<ContainerDto> list = new ArrayList<>();
        list.add(dto);
        return list;
    }

    @Override
    public void changeCalculationScheme(String id, String calculationSchemeId) throws DaoException {
        String sql="dao.ContainerDao.changeCalculationScheme";
        ContainerDto params=new ContainerDto();
        Date date=new Date();
        params.setId(id);
        params.setCalculationSchemeId(calculationSchemeId);
        params.setLastModifyTime(date);
        update(sql,params);
    }

    @Override
    public void changeDiskScheme(String id, String diskSchemeId) throws DaoException {
        String sql="dao.ContainerDao.changeDiskScheme";
        ContainerDto params=new ContainerDto();
        Date date=new Date();
        params.setId(id);
        params.setDiskSchemeId(diskSchemeId);
        params.setLastModifyTime(date);
        update(sql,params);
    }


    @Override
    public List<ContainerDto> selectContainerListByDiskSchemeId(String diskSchemeId) throws DaoException {
        String sql="dao.ContainerDao.selectContainerListByDiskSchemeId";
        Map<String,Object> params=new HashMap<>();
        params.put("diskSchemeId",diskSchemeId);
        return selectList(ContainerDto.class,sql,params);
    }

    @Override
    public List<ContainerDto> selectContainerListByCalculationSchemeId(String calculationSchemeId) throws DaoException {
        String sql="dao.ContainerDao.selectContainerListByCalculationSchemeId";
        Map<String,Object> params =new HashMap<>();
        params.put("calculationSchemeId",calculationSchemeId);
        return selectList(ContainerDto.class,sql,params);
    }
}
