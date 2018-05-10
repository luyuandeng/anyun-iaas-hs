package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.NetLabelInfoCreateParam;
import com.anyun.cloud.param.ProjectCreateParam;
import com.anyun.cloud.param.ProjectUpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.host.common.JdbcUtils;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.NetLabelInfoDao;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.dao.impl.NetLabelInfoDaoImpl;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.service.docker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-7-12.
 */
public class ProjectServiceImpl implements ProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Override
    public ProjectDto createProject(ProjectCreateParam param) throws Exception {
        ProjectDao dao = new ProjectDaoImpl();
        ProjectDto dto = new ProjectDto();
        String id = StringUtils.uuidGen();
        dto.setId(id);
        dto.setName(param.getName());
        dto.setDescript(param.getDescript());
        dto.setSpace(param.getSpace());
        dto.setUserUniqueId(param.getUserUniqueId());
        //创建 项目
        dao.insertProject(dto);
        try {
            //创建项目网络标签
            NetService netService = new NetServiceImpl();
            NetLabelInfoCreateParam netParam = new NetLabelInfoCreateParam();
            netParam.setProject(id);
            netParam.setGateway("");
            netParam.setSubnet("172.20.0.0/16");
            netParam.setName(param.getName() + ":网络");
            netParam.setDescr(param.getName() + ":网络");
            netService.insertNetLabelInfo(netParam);
            LOGGER.debug("项目网络标签创建成功！");
            return   queryProjectById(id);
        } catch (Exception e) {
            LOGGER.debug("项目网络标签创建失败:{}", e);
            //删除 已经创建的项目
            dao.deleteProjectById(id);
            throw new Exception("项目网络标签创建失败！[" + e.getMessage() + "]");
        }
    }

    @Override
    public ProjectDto updateProject(ProjectUpdateParam param) throws Exception {
        ProjectDao dao = new ProjectDaoImpl();
        ProjectDto dto = new ProjectDto();
        dto.setId(param.getId());
        dto.setName(param.getName());
        dto.setDescript(param.getDescript());
        dto.setSpace(param.getSpace());
        dto.setUserUniqueId(param.getUserUniqueId());
        ProjectDto projectDto =dao.updateProject(dto);
        return projectDto;
    }

    @Override
    public void deleteProject(String id) throws Exception {
        //删除数据库服务
        DatabaseService   databaseService=new DatabaseServiceImpl();
        databaseService.deleteDbByProjectId(id);

        //删除容器
        ContainerService containerService = new ContainerServiceImpl();
        containerService.deleteContainerByProject(id);

        //删除 网络标签
        NetService netService = new NetServiceImpl();
        netService.netLabelInfoDeleteByProject(id);

        //从项目表中删除项目
        ProjectDao dao = new ProjectDaoImpl();
        dao.deleteProjectById(id);
    }

    @Override
    public List<ProjectDto> queryProjectByCondition(String userUniqueId) throws Exception {
        ProjectDao dao = new ProjectDaoImpl();
        List<ProjectDto> list = dao.selectProjectByCondition(userUniqueId);
        if (list == null || list.isEmpty())
            return null;
        for (ProjectDto projectDto : list) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createTime = date.format(projectDto.getCreateDate());
            LOGGER.debug("时间：" + createTime);
            projectDto.setScreateDate(createTime);
            int runContainer = 0;
            ContainerDao containerDao = new ContainerDaoImpl();
            if (StringUtils.isEmpty(projectDto.getId()))
                continue;
            List<ContainerDto> containerDtoList = containerDao.selectContainerByProject(projectDto.getId(), 0);
            if (containerDtoList == null || containerDtoList.isEmpty())
                continue;
            for (ContainerDto c : containerDtoList) {
                if (StringUtils.isEmpty(c.getId()))
                    continue;
                if (c.getStatus() == 2)
                    runContainer++;
            }
            projectDto.setAllContanr(containerDtoList.size());
            projectDto.setRunContainer(runContainer);
        }
        return list;
    }

    @Override
    public ProjectDto queryProjectById(String id) throws Exception {
        ProjectDao dao = new ProjectDaoImpl();
        ProjectDto projectDto = dao.selectProjectById(id);
        if (projectDto == null){
            return null;
        }
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = date.format(projectDto.getCreateDate());
        projectDto.setScreateDate(createTime);
        int runContainer = 0;
        ContainerDao containerDao = new ContainerDaoImpl();
        List<ContainerDto> containerDtoList = containerDao.selectContainerByProject(projectDto.getId(), 0);
        if (containerDtoList != null && !containerDtoList.isEmpty()) {
            for (ContainerDto c : containerDtoList) {
                if (StringUtils.isEmpty(c.getId()))
                    continue;
                if (c.getStatus() == 2)
                    runContainer++;
            }
            projectDto.setAllContanr(containerDtoList.size());
            projectDto.setRunContainer(runContainer);
        }
        return projectDto;
    }

    @Override
    public ProjectInfoQueryDto projectInfoQueryById(String id) throws Exception {
        //根据项目查询 容器和网络绑定信息
        NetService netService = new NetServiceImpl();
        NetLabelInfoDao netLabelInfoDao = new NetLabelInfoDaoImpl();
        VSwitchService vSwitchService = new VSwitchServiceImpl();
        List<NetLabelIpContainerDto> netLabelIpContainerDtoList = netLabelInfoDao.selectContainerLabelIpByProject(id);
        if (netLabelIpContainerDtoList == null || netLabelIpContainerDtoList.isEmpty()) {
            LOGGER.debug("project no container connecting net");
            netLabelIpContainerDtoList = new ArrayList<>();
        } else {
            for (NetLabelIpContainerDto n : netLabelIpContainerDtoList) {
                NetLabelInfoDto netLabelInfoDto = netLabelInfoDao.selectNetLabelInfoByLabel(n.getNetLabel());
                if (!vSwitchService.networkLabelExist(netLabelInfoDto.getLabel(), netLabelInfoDto.getType())) {
                    netLabelIpContainerDtoList.remove(n);
                }
            }
            LOGGER.debug("project Container connect net size:[{}]", netLabelIpContainerDtoList.size());
        }

        //根据项目查询容器
        ContainerDao containerDao = new ContainerDaoImpl();
        List<ContainerDto> containerDtoList = containerDao.selectContainerByProject(id, 0);
        if (containerDtoList == null || containerDtoList.isEmpty()) {
            LOGGER.debug("project no container");
            containerDtoList = new ArrayList<>();
        } else {
            LOGGER.debug("project have container size:[{}]", containerDtoList.size());
        }

        List<NetLabelInfoDto> netLabelInfoDtoList = netService.netLabelInfoQueryAllBridge();//查询所有bridge类型标签
        if (netLabelInfoDtoList == null || netLabelInfoDtoList.isEmpty()) {
            LOGGER.debug("project no type[bridge] label");
            netLabelInfoDtoList = new ArrayList<>();
        } else
            LOGGER.debug("type[bridge]:{}", netLabelInfoDtoList.size());


        ProjectInfoQueryDto projectInfoQueryDto = new ProjectInfoQueryDto();
        projectInfoQueryDto.setNetLabelInfoDtoList(netLabelInfoDtoList);
        projectInfoQueryDto.setContainerDtoList(containerDtoList);
        projectInfoQueryDto.setNetLabelIpContainerDtoList(netLabelIpContainerDtoList);
        return projectInfoQueryDto;
    }

    @Override
    public List<ProjectDto> projectQuery(String subMethod, String subParameters) throws Exception {
        List<ProjectDto> l = null;
        switch (subMethod) {
            case "QUERY_BY_ID":
                ProjectDto p = queryProjectById(subParameters);
                if (p != null) {
                    l = new ArrayList<>();
                    l.add(p);
                }
                break;

            case "QUERY_ALL_UNDER_USER_BY_USERUNIQUEID":
                l = queryProjectByCondition(subParameters);
                break;
            default:
                throw new Exception("subMethod [" + subMethod + "] doer not exist");
        }

        return l;
    }

    @Override
    public PageDto<ProjectDto> getPageListConditions(CommonQueryParam param) throws Exception {
        PageDto<ProjectDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = null; //String  类型条件
        try {
            str = JdbcUtils.ListToString(param.getConditions());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.project_info_base.* FROM  anyuncloud.project_info_base  left   join   anyuncloud.tag_info   on  anyuncloud.project_info_base.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.project_info_base";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (param.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<ProjectDto> list = null;
            try {
                list = JdbcUtils.getList(ProjectDto.class, sql);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            total = (list == null ? 0 : list.size());
            pageDto.setPageSize(param.getLimit());
            pageDto.setPageNumber(param.getStart());
            pageDto.setTotal(total);
            return pageDto;
        } else { //分页查询
            int start = param.getStart();//查询页码
            int limit = param.getLimit();//每页记录
            boolean replyWithCount = param.isReplyWithCount();//是否返回总条数
            String sortBy = param.getSortBy();//排序字段
            String sortDirection = param.getSortDirection();//排序规则

            // 分页参数校验
            if (start <= 0)
                try {
                    throw new Exception("start is:{" + start + "}");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (limit <= 0)
                try {
                    throw new Exception("limit is:{" + limit + "}");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            int i = (start - 1) * limit;
            String pagingStatement = "limit" + " " + i + "," + limit;  //分页 语句

            //排序 语句
            String sortingStatement = (sortBy != null && !"".equals(sortBy) ? "order by" + " " + sortBy + " " + sortDirection : "");

            sql = selectStatement + " " + whereStatement + " ";
            //查询全部记录
            List<ProjectDto> list = null;
            try {
                list = JdbcUtils.getList(ProjectDto.class, sql);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<ProjectDto> l = null;
                try {
                    l = JdbcUtils.getList(ProjectDto.class, sql);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                pageDto.setData(l);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = list.size();
            }
            if (replyWithCount)
                pageDto.setTotal(total);
            return pageDto;
        }
    }

    @Override
    public ProjectDto queryProjectDtoByPlatFormNetworkId(String label) throws Exception {
       ProjectDao dao = new ProjectDaoImpl();
       return    dao.selectProjectDtoByPlatFormNetworkId(label);
    }

    @Override
    public List<ProjectDto> queryProjectByCondition() throws Exception {
        //TODO
        return null;
    }
}
