package test.com.anyun.sdk.platfrom;


import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.AssetsCreateParam;
import com.anyun.cloud.param.AssetsUpdateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.AssetsService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestAssetsService extends BaseAnyunTest{
    private AssetsService assetsService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        assetsService = factory.getAssetsService();
        ResourceClient.setUserToken("debug_token");
    }


    //------------------------------------------测试区域-------------------------------------------------

    /**
     * 根据id查询资产详情
     */
    @Test
    public void TeseQueryById()throws Exception{
        String id="11e15ca2c33341e9a1b0b567e5b6e1fa";
        String userUniqueId="";
        AssetsDto assetsDto = assetsService.queryById(id,userUniqueId);
        System.out.println(assetsDto.asJson());
    }

    /**
     * 2,根据分类查询资产
     */
    @Test
    public void TeseGetList()throws Exception{
        String deviceCategory="ALL";
        String userUniqueId = "";
        List<AssetsDto> assetsDtos = assetsService.getList(deviceCategory,userUniqueId);
        System.out.println(JsonUtil.toJson(assetsDtos));
    }

    /**
     * 3,根据条件分页查询资产
     * @throws Exception
     */
    @Test
    public void   getPageListConditions()throws Exception {
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
        commonQueryParam.setSortBy("");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("");

        List<Conditions> l = new ArrayList<>();

        Conditions conditions = new Conditions();
        conditions.setName("__userTag__");
        conditions.setOp("is null");
        conditions.setValue("%D%");
        l.add(conditions);


        Conditions conditions1 = new Conditions();
        conditions1.setName("deviceCategory");
        conditions1.setOp("is not null");
        conditions1.setValue("%S%");
        l.add(conditions1);

        Conditions conditions2 = new Conditions();
        conditions2.setName("");
        conditions2.setOp("");
        conditions2.setValue("");
        l.add(conditions2);

        commonQueryParam.setConditions(l);

        PageDto<AssetsDto> pageDto= assetsService.getPageListConditions(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    /**
     * 4,删除资产
     */
    @Test
    public void TestDelete(){
        String id="0ecb5fefe3ed4721b6b2da635aa12117";
        String userUniqueId="";
        Status<String> status = assetsService.deleteAssets(id,userUniqueId);
        System.out.println(status.getStatus());
    }

    /**
     * 5,批量创建资产
     */
    @Test
    public  void TestCreate(){
        List<AssetsCreateParam> params = new ArrayList<>();
        AssetsCreateParam param;

        /**
         * 服务器
         */
        param = new AssetsCreateParam();
        param.setUsed(0);
        param.setPurpose("Docker");
        param.setMaintenancePeriod("2019-12-12");
        param.setTelephone("1111");
        param.setSupplier("博康");
        param.setDeviceBelong("常州");
        param.setHardDisk("1*400G SSD，9*3TB");
        param.setMemory("256GB");
        param.setCpu("4*E7 4870");
        param.setPosition("j-7");
        param.setIpmiIp("172.16.1.13");
        param.setSystemIp("15.16.0.13");
        param.setCategoryDescription("虚拟化应用服务器");
        param.setDeviceSerialNumber("1212");
        param.setDeviceModel("fdkfjdf");
        param.setDeviceCategory("SERVER");
        param.setDisplayName("SERVER");
        param.setDeviceName("SERVER");
        params.add(param);

        /**
         * 路由器
         */
        param = new AssetsCreateParam();
        param.setUsed(0);
        param.setPurpose("Docker");
        param.setMaintenancePeriod("2019-12-19");
        param.setTelephone("2222");
        param.setSupplier("博康");
        param.setDeviceBelong("常州");
        param.setPosition("j-7");
        param.setManagementIp("192.168.1.1");
        param.setCategoryDescription("虚拟化应用服务器");
        param.setDeviceModel("fdkfjdf");
        param.setDeviceCategory("");
        param.setDisplayName("ROUTER");
        param.setDeviceName("ROUTER");
        params.add(param);
        /**
         * 交换机
         */
        param = new AssetsCreateParam();
        param.setUsed(0);
        param.setPurpose("Docker");
        param.setMaintenancePeriod("2019-12-19");
        param.setTelephone("3333");
        param.setSupplier("博康");
        param.setDeviceBelong("常州");
        param.setPosition("j-7");
        param.setManagementIp("192.168.1.1");
        param.setCategoryDescription("虚拟化应用服务器");
        param.setDeviceModel("fdkfjdf");
        param.setDeviceCategory("SWITCH");
        param.setDisplayName("SWITCH");
        param.setDeviceName("SWITCH");
        params.add(param);


        List<AssetsDto> list= assetsService.createAssets(params);
        System.out.println(JsonUtil.toJson(list));

    }

    /**
     * 6、修改资产
     * */
    @Test
    public  void TestUpdate(){
        AssetsUpdateParam param = new AssetsUpdateParam();
        param.setId("46af72b9daea4a629d4c387c94ac6e64");
//        param.setUsed(0);
//        param.setPurpose("");
//        param.setDisplayName("");
//        param.setDeviceName("");
//        param.setDeviceSerialNumber("");
//        param.setDeviceModel("");
//        param.setCategoryDescription("虚拟化1111");
//        param.setManagementIp("");
//        param.setIpmiIp("");
//        param.setSystemIp("");
//        param.setPosition("");
//        param.setCpu("");
//        param.setMemory("");
//        param.setHardDisk("");
//        param.setDeviceBelong("常州111");
//        param.setSupplier("");
//        param.setTelephone("1212121");
//        param.setMaintenancePeriod("");
        AssetsDto dto = assetsService.updateArea(param);
        System.out.print(JsonUtil.toJson(dto));
    }




    /**
     * 批量删除
     */
    @Test
    public  void TestDeleteList(){
        List<String> ids = new ArrayList<>();
        ids.add("11e15ca2c33341e9a1b0b567e5b6e1fa");
        String userUniqueId="";
        Status<String> status = assetsService.deleteListAssets(ids,userUniqueId);
    }

}
