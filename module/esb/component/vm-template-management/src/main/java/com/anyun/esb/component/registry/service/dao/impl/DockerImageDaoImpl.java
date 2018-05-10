package com.anyun.esb.component.registry.service.dao.impl;

import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.registry.service.dao.BaseMyBatisDao;
import com.anyun.esb.component.registry.service.dao.DockerImageDao;
import com.anyun.exception.DaoException;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Transient;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class DockerImageDaoImpl extends BaseMyBatisDao implements DockerImageDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageDaoImpl.class);

    @Override
    public PageDto<DockerImageDto> selectImageByConditions(String categoryType, String templateName, int pageNo, int pageSize) {
        PageDto pageDto = new PageDto();
        String sql = "dao.DockerImageDao.selectImageByConditions";
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("categoryType", categoryType);
        conditions.put("templateName", templateName);
        if (pageSize == 0 || pageNo == 0) {
            List<DockerImageDto> allDtos = selectList(DockerImageDto.class, sql, conditions);
            pageDto.setTotal(allDtos.size());
            pageDto.setPageSize(-1);
            pageDto.setPageNumber(-1);
            pageDto.setData(allDtos);
            return pageDto;
        }
        Page<DockerImageDto> page = selectPageList(DockerImageDto.class, sql, conditions, pageNo, pageSize);
        pageDto.setPageSize(pageSize);
        pageDto.setPageNumber(pageNo);
        pageDto.setTotal(Long.valueOf(page.getTotal()).intValue());
        pageDto.setData(page.getResult());
        System.out.print("pageDto="+JsonUtil.toJson(pageDto));
        return pageDto;
    }

    @Override
    public DockerImageDto selectById(String id) {
        String sql = "dao.DockerImageDao";
        return selectById(DockerImageDto.class,sql,id);
    }


    @Override
    public DockerImageDto updateStatus(DockerImageDto dockerImageDto) throws DaoException {
        String sql = "dao.DockerImageDao.updateStatus";
        dockerImageDto.setLastModifyDate(new Date());
        update(sql, dockerImageDto);
        return selectById(dockerImageDto.getId());
    }

    @Override
    @Transient
    public void batchUpdateStatus(List<String> ids, int status) throws DaoException {
        for (String id : ids) {
            String sql = "dao.DockerImageDao.updateStatus";
            Map<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("id", id);
            conditions.put("status", status);
            conditions.put("lastModifyDate", new Date());
            update(sql, conditions);
        }
    }

    @Override
    public void update(DockerImageDto dto) throws DaoException {
        String sql = "dao.DockerImageDao.update";
        Date date = new Date();
        dto.setLastModifyDate(date);
        update(sql, dto);
    }

    @Override
    public List<DockerImageDto> selectByFullName(String category, String name) {
        String sql = "dao.DockerImageDao.selectByFullName";
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("category", category);
        conditions.put("name", name);
        return selectList(DockerImageDto.class, sql, conditions);
    }

    @Override
    public List<DockerImageDto> DockerImageQueryRegistry() {
        String sql="dao.DockerImageDao.DockerImageQueryRegistry";
        return selectList(DockerImageDto.class,sql,"");
    }

    @Override
    public List<DockerImageDto> selectIamgeByCategory(String name) throws DaoException {
        String sql = "dao.DockerImageDao.selectIamgeByCategory";
        return selectList(DockerImageDto.class,sql,name);
    }

    @Override
    public void insertUnregistDockerImages(DockerImageDto dto1) throws DaoException {
        String sql = "dao.DockerImageDao.insertUnregistDockerImages";
        insert(sql,dto1);
    }

    @Override
    public List<DockerImageDto> selectUnregistDockerImages() throws DaoException {
        String sql = "dao.DockerImageDao.selectUnregistDockerImages";
        return selectList(DockerImageDto.class,sql,null);
    }
}
