package test.com.anyun.host;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.dao.AssetsDao;
import com.anyun.esb.component.host.dao.impl.AssetsDaoImpl;
import com.anyun.esb.component.host.service.docker.AssetsService;
import com.anyun.esb.component.host.service.docker.impl.AssetsServiceImpl;
import com.anyun.esb.component.host.service.publish.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jt-workspace on 17-4-13.
 */
public class testAssets extends BaseComponentTest  {
    private AssetsService assetsService;
    private AssetsDeleteByIdService assetsDeleteByIdService;
    private AssetsQueryByIdService assetsQueryByIdService;
    private AssetsQueryByDeviceCategoryService assetsQueryByDeviceCategoryService;
    private AssetsQueryByConditionsService assetsQueryByConditionsService;
    private AssetsDeleteByListService assetsDeleteByListService;

    @Before
    public void setup() throws Exception {
        assetsService = new AssetsServiceImpl();
        assetsDeleteByIdService = new AssetsDeleteByIdService();
        assetsQueryByIdService = new AssetsQueryByIdService();
        assetsQueryByDeviceCategoryService = new AssetsQueryByDeviceCategoryService();
        assetsQueryByConditionsService = new AssetsQueryByConditionsService();
        assetsDeleteByListService = new AssetsDeleteByListService();
    }

    //根据id删除Assets
    @Test
    public void testAssetsDeleteService(){
//        Map<String,Object> param =new HashMap();
//        param.put("id","1");
//        param.put("userUniqueId","");
//        exchange.getIn().setHeaders(param);
//        Status<String> status= (Status)assetsDeleteByIdService.process(endpoint,exchange);
//        System.out.println(status.getStatus());
        AssetsDao assetsDao;
        assetsDao = new AssetsDaoImpl();
        String id="1";
        assetsDao.deleteAsserts(id);
    }

    //根据id查询 chaxun Assets
    @Test
    public void testAssetsQueryByidService() {
        Map<String, Object> m = new HashMap();
        m.put("userUniqueId", "");
        m.put("id", "2");
        exchange.getIn().setHeaders(m);
        List<AssetsDto> dtos = (List<AssetsDto>) assetsQueryByIdService.process(endpoint, exchange);
        System.out.println(JsonUtil.toJson(dtos));
//        AssetsDao assetsDao;
//        assetsDao = new AssetsDaoImpl();
//        String id="1";
//        assetsDao.QueryAssetsInfo(id);
    }

    //根据deviceCategory删查询 chaxun Assets
    @Test
    public void testAssetsQueryBydeviceCategoryService() {
        Map<String, Object> m = new HashMap();

        m.put("userUniqueId", "");
        m.put("deviceCategory", " ALL");
        exchange.getIn().setHeaders(m);
        List<AssetsDto> dtos = (List<AssetsDto>) assetsQueryByDeviceCategoryService.process(endpoint, exchange);
        System.out.println(JsonUtil.toJson(dtos));
    }


    //根据condition chaxun Assets
    @Test
    public void testAreaQueryByConditions() {
        //{name:“__userTag__",op:"in",value:"tag1,tag2"}
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
        commonQueryParam.setSortBy("id");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");

        List<Conditions> l = new ArrayList<>();

        Conditions conditions = new Conditions();
        conditions.setName("__userTag__");
        conditions.setOp("is null");
        conditions.setValue("%D%");
//        l.add(conditions);

        Conditions conditions1 = new Conditions();
        conditions1.setName("type");
        conditions1.setOp("like");
        conditions1.setValue("%D%");
//        l.add(conditions1);

        Conditions conditions2 = new Conditions();
        conditions2.setName("name");
        conditions2.setOp("is not null");
        conditions2.setValue("1");
//        l.add(conditions2);


        Conditions conditions4 =new Conditions();
        conditions4.setName("deviceCategory");
        conditions4.setOp("is not null");
        conditions4.setValue("SWITCH");
        l.add(conditions4);


        Conditions conditions3 = new Conditions();
        conditions3.setName("id");
        conditions3.setOp("not in");
        String test1 = "";
        String test2 =  "";
        String test3 = "";
        conditions3.setValue("(" + test1 + "," + test2 +"," + test3+")");
//        l.add(conditions3);

        commonQueryParam.setConditions(l);
        exchange.getIn().setHeader("s", commonQueryParam.asJson());
        PageDto<AssetsDto> pageDto = (PageDto<AssetsDto>) assetsQueryByConditionsService.process(endpoint, exchange);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    @Test
    public void testAssetsDeleteByListService() {
        Map<String,Object> param =new HashMap();
        param.put("id","1,3658ce78432842cf9735659160f7a6a4,3,4,5,6,7, , ");
        param.put("userUniqueId","");
        exchange.getIn().setHeaders(param);
        Status<String> status = (Status<String>) assetsDeleteByListService.process(endpoint, exchange);
        System.out.println(status.getStatus());

    }
}
