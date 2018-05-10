package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.NetService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-7-28.
 */
public class TestNet extends BaseAnyunTest {
    private NetService service;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        service = factory.getNetService();
        ResourceClient.setUserToken("debug_token");
    }


    //1、查询 网络标签详情
    @Test
    public void getNetLabelInfoDetails() {
        String userUniqueId = "";
        String label = "anyun-in";
        NetLabelInfoDto netLabelInfoDto = service.getNetLabelInfoDetails(label, userUniqueId);
        System.out.print("netLabelInfoDto:" + JsonUtil.toJson(netLabelInfoDto));
    }

    //2、查询  网络标签列表
    @Test
    public void getNetLabelInfoList() {
        String userUniqueId = "";
        String type = "bridge";
        List<NetLabelInfoDto> l = service.getNetLabelInfoList(type, userUniqueId);
        System.out.print("l:" + JsonUtil.toJson(l));
    }


    //3、查询 容器ip 列表
    @Test
    public void getContainerIpInfoList() {
        //QUERY_BY_LABEL_CONTAINER
        //QUERY_ALREADY_ADDED_BY_LABEL
        //QUERY_NOT_ADD_BY_LABEL
        String userUniqueId = "";
        String subMethod = "QUERY_NOT_ADD_BY_LABEL";
        String subParameters = "anyun-ypzr0AW0K78";
        List<ContainerIpInfoDto> l = service.getContainerIpInfoList(userUniqueId, subMethod, subParameters);
        System.out.print(JsonUtil.toJson(l));
    }


    //4、将容器绑定到网络标签
    @Test
    public void testContainerConnectToNetwork() {
        ConnectToNetParam param = new ConnectToNetParam();
        param.setContainer("573776b184236511c050d3d433da02483c0a54853949cb3e2661a4fff5255631");
        param.setLabel("anyun-in");
        param.setIp("192.168.1.254");
        param.setUserUniqueId("2");
        Status<String> s = service.containerConnectToNetwork(param);
        System.out.print(s.getStatus());
    }

    //5、 将容器从OVN标签移除
    @Test
    public void testContainerDisconnectFromNetwork() {
        DisconnectFromNetParam param = new DisconnectFromNetParam();
        param.setContainer("96276d42363403268e31a9e3a034d0088d15f9e1ca13c87704424963fd14db28");
        param.setLabel("anyun-in");
        param.setUserUniqueId("2");
        Status<String> s = service.containerDisconnectFromNetwork(param);
        System.out.print(s.getStatus());
    }

    /**
     * 6、查询容器ip列表
     * */
    @Test
    public void   getContainerIpPageListConditions()throws Exception{
//        {name:“__userTag__",op:"in",value:"tag1,tag2"}
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
        commonQueryParam.setSortBy("id");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");
//
        List<Conditions> l = new ArrayList<>();
//
        Conditions conditions = new Conditions();
        conditions.setName("__userTag__");
        conditions.setOp("is null");
        conditions.setValue("%D%");
        l.add(conditions);
//
//        Conditions conditions1 = new Conditions();
//        conditions1.setName("type");
//        conditions1.setOp("like");
//        conditions1.setValue("%D%");
//        l.add(conditions1);
//
//        Conditions conditions2 = new Conditions();
//        conditions2.setName("name");
//        conditions2.setOp("is not null");
//        conditions2.setValue("11");
//        l.add(conditions2);
//
//
//        Conditions conditions3 = new Conditions();
//        conditions3.setName("status");
//        conditions3.setOp("not in");
//        String test1 = "1";
//        String test2 = "2";
//        conditions3.setValue("(" + test1 + "," + test2 + ")");
//        l.add(conditions3);
//
        commonQueryParam.setConditions(l);


        PageDto<ContainerIpInfoDto> pageDto= service.getContainerIpList(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    /**
     * 7、查询L3网络列表
     * */
    @Test
    public void   getL3PageListConditions()throws Exception{
//        {name:“__userTag__",op:"in",value:"tag1,tag2"}
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
//        commonQueryParam.setSortBy("name");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");
//
        List<Conditions> l = new ArrayList<>();
//
        Conditions conditions = new Conditions();
        conditions.setName("__userTag__");
        conditions.setOp("is null");
        conditions.setValue("%D%");
        l.add(conditions);
//
//        Conditions conditions1 = new Conditions();
//        conditions1.setName("type");
//        conditions1.setOp("like");
//        conditions1.setValue("%D%");
//        l.add(conditions1);
//
//        Conditions conditions2 = new Conditions();
//        conditions2.setName("name");
//        conditions2.setOp("is not null");
//        conditions2.setValue("11");
//        l.add(conditions2);
//
//
//        Conditions conditions3 = new Conditions();
//        conditions3.setName("status");
//        conditions3.setOp("not in");
//        String test1 = "1";
//        String test2 = "2";
//        conditions3.setValue("(" + test1 + "," + test2 + ")");
//        l.add(conditions3);
//
        commonQueryParam.setConditions(l);
        PageDto<NetLabelInfoDto> pageDto= service.QueryNetLabelInfoList(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }
}
