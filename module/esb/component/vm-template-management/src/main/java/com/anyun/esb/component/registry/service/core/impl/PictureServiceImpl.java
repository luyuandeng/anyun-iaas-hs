package com.anyun.esb.component.registry.service.core.impl;

import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.PictureDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.PictureUploadParam;
import com.anyun.esb.component.registry.common.JdbcUtils;
import com.anyun.esb.component.registry.service.core.PictureService;
import com.anyun.esb.component.registry.service.dao.PictureDao;
import com.anyun.esb.component.registry.service.dao.impl.PictureDaoImpl;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;


/**
 * Created by sxt on 16-10-17.
 */
public class PictureServiceImpl  implements PictureService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PictureServiceImpl.class);
    private PictureDao pictureDao=new PictureDaoImpl();

    @Override
    public PictureDto pictureUpload(PictureUploadParam param) throws Exception {
        PictureDto pictureDto = new PictureDto();
//        String urlFile = "";//文件保存的路径
//        String maxPicUrl = "";//返回的路径
//        String filePath = param.getPath()+param.getName();
//        String SavePath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\resource\\pic\\";
//        URL url =new URL("http://localhost:8080/resource/pic/");
//        File uploadDir= new File(SavePath);
//        File file = new File(filePath);
//        if(!file.isFile()){
//            throw new Exception("不是文件格式");
//        }
//        if(!uploadDir.exists()){
//            uploadDir.mkdir();
//        }
//        BufferedImage image = ImageIO.read(file);
//        if(image != null){
//            BufferedImage tag = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
//            tag.getGraphics().drawImage(image,0,0,image.getWidth(),image.getHeight(),null);
//            int lastLength = filePath.lastIndexOf(".");
//            Date date = new Date(System.currentTimeMillis());
//            String strDate = new SimpleDateFormat("yyyyMMddhhmmss").format(date);
//            int random = (int)(Math.random()*99);
//            String imageName = strDate + random;//以系统时间来命名
//            String fileType = filePath.substring(lastLength);
//            urlFile = SavePath + imageName + fileType;
//            maxPicUrl =url+ imageName + fileType;
//            LOGGER.debug(maxPicUrl);
//
//            FileOutputStream out = new FileOutputStream(urlFile);
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            JPEGEncodeParam encodeParam = encoder.getDefaultJPEGEncodeParam(tag);
//            encodeParam.setQuality(0.95f, true); //95%图像
//            encodeParam.setDensityUnit(1); //像素尺寸单位.像素/英寸
//            encodeParam.setXDensity(300); //水平分辨率
//            encodeParam.setYDensity(300); //垂直分辨率
//
//            encoder.setJPEGEncodeParam(encodeParam);
//            encoder.encode(tag);
//            pictureDto.setMaxPicUrl(maxPicUrl);
//            tag.flush();
//            out.flush();
//            out.close();
//        }
        return pictureDto;
    }

    @Override
    public List<PictureDto> pictureQueryAll() throws Exception {
        return    pictureDao.pictureSelectAll();
    }

    @Override
    public PageDto<PictureDto> getPageListConditions(CommonQueryParam param) throws RestfulApiException {
        PageDto<PictureDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = null; //String  类型条件
        try {
            str = JdbcUtils.ListToString(param.getConditions());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.picture_info_base.* FROM  anyuncloud.picture_info_base  left   join   anyuncloud.tag_info   on  anyuncloud.picture_info_base.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.picture_info_base";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (param.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<PictureDto> list = null;
            try {
                list = JdbcUtils.getList(PictureDto.class, sql);
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
            List<PictureDto> list = null;
            try {
                list = JdbcUtils.getList(PictureDto.class, sql);
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
                List<PictureDto> l = null;
                try {
                    l = JdbcUtils.getList(PictureDto.class, sql);
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
}
