package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.esb.component.host.common.JdbcUtils;
import com.anyun.cloud.param.AssetsCreateParam;
import com.anyun.cloud.param.AssetsUpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.dao.AssetsDao;
import com.anyun.esb.component.host.dao.impl.AssetsDaoImpl;
import com.anyun.esb.component.host.service.docker.AssetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twh-workspace on 17-4-10.
 */
public class AssetsServiceImpl implements AssetsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetsServiceImpl.class);
    private AssetsDao assetsDao = new AssetsDaoImpl();


    @Override
    public AssetsDto QueryAssetsInfo(String id)throws Exception  {
        return assetsDao.QueryAssetsInfo(id);
    }

    @Override
    public List<AssetsDto> QueryAssetsBydeviceCategory(String deviceCategory) throws Exception {
        String s1=deviceCategory;
        String s2="ALL";
        int result;
        int len1 = s1.length();
        int len2 = s2.length();
        int lim = Math.min(len1, len2);
        char[] charS1 = s1.toCharArray();
        char[] charS2 = s2.toCharArray();
        int k = 0;
        while (k < lim) {
            char c1 = charS1[k];
            char c2 = charS2[k];
            if (c1 != c2)
                result= c1 - c2;
            k++;
        }
        result= len1 - len2;
        if (result==0)
        return assetsDao.QueryAssetsAll(deviceCategory);
        else
            return assetsDao.QueryAssetsBydeviceCategory(deviceCategory);
    }

    @Override
    public PageDto<AssetsDto> QueryAssetsByCondition(CommonQueryParam commonQueryParam) throws Exception {
        PageDto<AssetsDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//sql查询语句
        String str = JdbcUtils.ListToString(commonQueryParam.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.assets_info_base.* FROM  anyuncloud.assets_info_base  left   join   anyuncloud.tag_info   on  anyuncloud.assets_info_base.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.assets_info_base";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (commonQueryParam.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<AssetsDto> list = JdbcUtils.getList(AssetsDto.class, sql);
            total = (list == null ? 0 : list.size());
            pageDto.setPageSize(commonQueryParam.getLimit());
            pageDto.setPageNumber(commonQueryParam.getStart());
            pageDto.setTotal(total);
            return pageDto;
        }
        else{
            //分页查询
            int start = commonQueryParam.getStart();//查询页码
            int limit = commonQueryParam.getLimit();//每页记录
            boolean replyWithCount = commonQueryParam.isReplyWithCount();//是否返回总条数
            String sortBy = commonQueryParam.getSortBy();//排序字段
            String sortDirection = commonQueryParam.getSortDirection();//排序规则

            // 分页参数校验
            if (start <= 0)
                throw new Exception("start is:{" + start + "}");
            if (limit <= 0)
                throw new Exception("limit is:{" + limit + "}");
            int i = (start - 1) * limit;
            String pagingStatement = "limit" + " " + i + "," + limit;  //分页 语句

            //排序 语句
            String sortingStatement = (sortBy != null && !"".equals(sortBy) ? "order by" + " " + sortBy + " " + sortDirection : "");

            sql = selectStatement + " " + whereStatement + " ";
            //查询全部记录
            List<AssetsDto> list = JdbcUtils.getList(AssetsDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            }else{
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<AssetsDto> l = JdbcUtils.getList(AssetsDto.class, sql);
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
    public void deleteAsserts(String id) throws Exception {
        assetsDao.deleteAsserts(id);
    }

    @Override
    public List<AssetsDto> createAssets(List<AssetsCreateParam> param) throws Exception {
        List<AssetsDto> list = new ArrayList<>();
        for(int i =0;i<param.size();i++){
            String id = StringUtils.uuidGen();
            AssetsDto assetsDto = new AssetsDto();
            AssetsDto assetsDto1 = new AssetsDto();
            assetsDto.setId(id);
            assetsDto.setUsed(param.get(i).getUsed());
            assetsDto.setPurpose(param.get(i).getPurpose());
            assetsDto.setDisplayName(param.get(i).getDisplayName());
            assetsDto.setDeviceName(param.get(i).getDeviceName());
            assetsDto.setDeviceSerialNumber(param.get(i).getDeviceSerialNumber());
            assetsDto.setDeviceModel(param.get(i).getDeviceModel());
            assetsDto.setDeviceCategory(param.get(i).getDeviceCategory());
            assetsDto.setCategoryDescription(param.get(i).getCategoryDescription());
            assetsDto.setManagementIp(param.get(i).getManagementIp());
            assetsDto.setSystemIp(param.get(i).getSystemIp());
            assetsDto.setIpmiIp(param.get(i).getIpmiIp());
            assetsDto.setPosition(param.get(i).getPosition());
            assetsDto.setCpu(param.get(i).getCpu());
            assetsDto.setMemory(param.get(i).getMemory());
            assetsDto.setHardDisk(param.get(i).getHardDisk());
            assetsDto.setDeviceBelong(param.get(i).getDeviceBelong());
            assetsDto.setSupplier(param.get(i).getSupplier());
            assetsDto.setTelephone(param.get(i).getTelephone());
            assetsDto.setMaintenancePeriod(param.get(i).getMaintenancePeriod());
            assetsDto1 = assetsDao.createAssets(assetsDto).get(0);
            list.add(assetsDto1);
        }
        return list;
    }

    @Override
    public AssetsDto updateAsserts(AssetsUpdateParam param) throws Exception {
        AssetsDto assetsDto =new AssetsDto();
        assetsDto.setId(param.getId());
        assetsDto.setUsed(param.getUsed());
        assetsDto.setPurpose(param.getPurpose());
        assetsDto.setDisplayName(param.getDisplayName());
        assetsDto.setDeviceName(param.getDeviceName());
        assetsDto.setDeviceSerialNumber(param.getDeviceSerialNumber());
        assetsDto.setDeviceModel(param.getDeviceModel());
        assetsDto.setCategoryDescription(param.getCategoryDescription());
        assetsDto.setManagementIp(param.getManagementIp());
        assetsDto.setSystemIp(param.getSystemIp());
        assetsDto.setIpmiIp(param.getIpmiIp());
        assetsDto.setPosition(param.getPosition());
        assetsDto.setCpu(param.getCpu());
        assetsDto.setMemory(param.getMemory());
        assetsDto.setHardDisk(param.getHardDisk());
        assetsDto.setDeviceBelong(param.getDeviceBelong());
        assetsDto.setSupplier(param.getSupplier());
        assetsDto.setTelephone(param.getTelephone());
        assetsDto.setMaintenancePeriod(param.getMaintenancePeriod());
        return assetsDao.updateAssets(assetsDto);
    }
}
