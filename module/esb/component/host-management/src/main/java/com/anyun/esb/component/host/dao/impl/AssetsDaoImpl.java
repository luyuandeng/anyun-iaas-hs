package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.AssetsDto;
import com.anyun.esb.component.host.dao.AssetsDao;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twh-workspace on 17-4-10.
 */
public class AssetsDaoImpl extends BaseMyBatisDao implements AssetsDao {


    @Override
    public AssetsDto QueryAssetsInfo(String id) {
        String sql = "dao.AssetsDao.selectAssetsById";
        return selectOne(AssetsDto.class, sql, id);
    }

    @Override
    public List<AssetsDto> QueryAssetsBydeviceCategory(String deviceCategory) {

            String Sql="dao.AssetsDao.selectAssetsByDeviceCategory";
            List<AssetsDto> List=selectList(AssetsDto.class, Sql, deviceCategory);
            return List;
    }

    @Override
    public List<AssetsDto> QueryAssetsAll(String deviceCategory) {
        String sql ="dao.AssetsDao.selectAssetsAll";
        List<AssetsDto>list=selectList(AssetsDto.class,sql,deviceCategory);
        return list;
    }


    @Override
    public void deleteAsserts(String id) {
        String sql = "dao.AssetsDao.deleteAssetsById";
        delete(sql, id);
    }

    public List<AssetsDto> createAssets(AssetsDto assetsDto) throws Exception {
        String sql = "dao.AssetsDao.createAssets";
        insert(sql,assetsDto);
        AssetsDto assetsDto1 = QueryAssetsInfo(assetsDto.getId());
        List<AssetsDto> list =new ArrayList<>();
        list.add(assetsDto1);
        return list;
    }

    @Override
    public AssetsDto updateAssets(AssetsDto assetsDto) throws Exception {
        String sql = "dao.AssetsDao.updateAssets";
        update(sql,assetsDto);
        return QueryAssetsInfo(assetsDto.getId());
    }
}
