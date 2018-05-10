package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AreaDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.AreaCreateParam;
import com.anyun.cloud.param.AreaUpdateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.AreaService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gp on 17-3-28.
 */
public class TestArea extends BaseAnyunTest {
    private AreaService areaService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        areaService = factory.getAreaServie();
        ResourceClient.setUserToken("debug_token");
    }

    //------------------------------------------区域测试------------------------------------------------------------
    /**
     * 1、查询区域详情
     * */
    @Test
    public void TeseQueryById()throws Exception{
        String id="b78bc1ee525b4bf29eae16d8c4b651c7";
        String userUniqueId="2";
        AreaDto areaDto = areaService.queryById(id,userUniqueId);
        System.out.println(areaDto.asJson());
    }




    /**
     * 2、查询区域列表
     * */
    @Test
    public void TeseGetList()throws Exception{
        String type="KVM";
        String status="Enable";
        String userUniqueId="";
        List<AreaDto> areaDto = areaService.getList(type,status,userUniqueId);
        System.out.println(JsonUtil.toJson(areaDto));
    }




    /**
     * 3、删除区域
     * */
    @Test
    public void TestDelete(){
        String id="b78bc1ee525b4bf29eae16d8c4b651c7";
        String userUniqueId="";
        Status<String> status = areaService.deleteArea(id,userUniqueId);
        System.out.println(status.getStatus());
    }




    /**
     * 4、创建区域
     * */
    @Test
    public  void TestCreate(){
        AreaCreateParam param=new AreaCreateParam();
        param.setName("test2");
        param.setDescription("name");
        param.setStatus("Enable");
        param.setType("DOCKER");
        Status<String> status = areaService.createArea(param);
        System.out.print(status.getStatus());
    }


    /**
     * 5、修改区域
     * */
    @Test
    public  void TestUpadte(){
        AreaUpdateParam param = new AreaUpdateParam();
        param.setId("b78bc1ee525b4bf29eae16d8c4b651c7");
        param.setName("1213");
        Status<String> status = areaService.updateArea(param);
        System.out.print(status.getStatus());
    }



    /**
     * 6、修改状态
     * */
    @Test
    public  void TestChange(){
        AreaUpdateParam param = new AreaUpdateParam();
        param.setId("b78bc1ee525b4bf29eae16d8c4b651c7");
        param.setStatus("Disable");
        Status<String> status = areaService.changeAreaStatus(param);
        System.out.print(status.getStatus());
    }

    /**
     * 7、查询区域列表
     * */
    @Test
    public void   getPageListConditions()throws Exception{
//        {name:“__userTag__",op:"in",value:"tag1,tag2"}
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
        commonQueryParam.setSortBy("name");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");

        List<Conditions> l = new ArrayList<>();

        Conditions conditions = new Conditions();
        conditions.setName("__userTag__");
        conditions.setOp("is null");
        conditions.setValue("%D%");
        l.add(conditions);

        Conditions conditions1 = new Conditions();
        conditions1.setName("type");
        conditions1.setOp("like");
        conditions1.setValue("%D%");
        l.add(conditions1);

        Conditions conditions2 = new Conditions();
        conditions2.setName("name");
        conditions2.setOp("is not null");
        conditions2.setValue("11");
        l.add(conditions2);


        Conditions conditions3 = new Conditions();
        conditions3.setName("status");
        conditions3.setOp("in");
        String test1 = "";
        String test2 = "";
        conditions3.setValue("(" + test1 + "," + test2 + ")");
        l.add(conditions3);

        commonQueryParam.setConditions(l);


        PageDto<AreaDto> pageDto= areaService.getPageListConditions(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }
}
