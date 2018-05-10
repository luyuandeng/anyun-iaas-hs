package com.anyun.esb.component.registry.service.dao.impl;

import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.esb.component.registry.service.dao.BaseMyBatisDao;
import com.anyun.esb.component.registry.service.dao.DockerImageCategoryDao;
import com.anyun.exception.DaoException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class DockerImageCategoryDaoImpl extends BaseMyBatisDao implements DockerImageCategoryDao {
    @Override
    public DockerImageCategoryDto selectById(String id) {
        String sql = "dao.DockerImageCategoryDao";
        return selectById(DockerImageCategoryDto.class,sql,id);
    }

    @Override
    public DockerImageCategoryDto updateStatus(DockerImageCategoryDto dockerImageCategoryDto) throws DaoException {
        String sql = "dao.DockerImageCategoryDao.updataStatus";
        dockerImageCategoryDto.setDateLastModify(new Date());
        update(sql,dockerImageCategoryDto);
        return  selectById(dockerImageCategoryDto.getId());
    }

    @Override
    public void batchDockerImageCategoriesStatus(List<String> ids,int status) {
        for (String id:ids) {
            String sql ="dao.DockerImageCategoryDao.deleteStatus";
            Map<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("id", id);
            conditions.put("status", status);
            conditions.put("lastModifyDate", new Date());
            update(sql,conditions);
        }
    }

    @Override
    public DockerImageCategoryDto update(DockerImageCategoryDto dockerImageCategoryDto) {
        String sql = "dao.DockerImageCategoryDao.update";
        update(sql,dockerImageCategoryDto);
        return selectById(dockerImageCategoryDto.getId());
    }

    @Override
    public List<DockerImageCategoryDto> selectRegistryCategories() throws  DaoException{
        String sql="dao.DockerImageCategoryDao.selectRegistryCategories";
        return  selectList(DockerImageCategoryDto.class,sql,null);
    }

    @Override
    public void insertUnregistDockerImageCategories(DockerImageCategoryDto dto1) throws DaoException {
        String sql = "dao.DockerImageCategoryDao.insertCategory";
        insert(sql,dto1);
    }

    @Override
    public List<DockerImageCategoryDto> selectUnregistCategories() throws DaoException {
        String sql="dao.DockerImageCategoryDao.selectUnregistCategories";
        return selectList(DockerImageCategoryDto.class,sql,null);
    }

}
