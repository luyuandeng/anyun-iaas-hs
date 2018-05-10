package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ApplicationInfoDto;
import com.anyun.cloud.dto.ApplicationInfoLoadDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.ApplicationService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import javafx.application.Application;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gp on 16-10-26.
 */
public class TestApplicationService extends BaseAnyunTest {
    private ApplicationService service;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        service = factory.getApplicationService();
        ResourceClient.setUserToken("debug_token");
    }

    //1、查询详情
    @Test
    public void getDetails() throws Exception {
        String id = "9f81b9abc30244f0b02621f34ab9856a";
        String userUniqueId = "2";
        ApplicationInfoDto dto = service.applicationQueryById(id, userUniqueId);
        System.out.print(JsonUtil.toJson(dto));
    }


    //2、查询应用列表
    @Test
    public void getList() throws Exception {
        String project = "b0cc3c8a76804e639074bf38f93bc0b3";
        String userUniqueId = "";
        List<ApplicationInfoDto> list = service.applicationQueryByProject(project, userUniqueId);
        for (ApplicationInfoDto a : list) {
            System.out.print(JsonUtil.toJson(a));
        }
    }

    //3、删除应用
    @Test
    public void delete() throws Exception {
        String id = "0ad172b77748496d260ba32460ea484f77e095493c73f83b8f4fd7bc2efcff28";
        String userUniqueId = "2";
        Status<String> s = service.applicationDeleteById(id, userUniqueId);
        System.out.print(s.getStatus());
    }

    // 4、创建应用
    @Test
    public void create() throws Exception {
        String userUniqueId = "";
        ApplicationCreateParam param = new ApplicationCreateParam();
        param.setName("测试");
        param.setDescription("测试");
        param.setTemplateContainer("58d96f5a790cba60d481bbd1a171f008c977259f7648027796d0945098992715");
        param.setIp("192.168.1.215");
        param.setLabel("anyun-in");
        param.setNginxPort(80);
        param.setLoadPort(80);
        param.setType("WEB");
        param.setLoadsTotal(3);
        param.setWeightType("ip_hash");
//        param.setCertificate("test");
//        param.setPrivateKey("test");
        param.setUserUniqueId("6");
        ApplicationInfoDto s = service.applicationCreate(param);
        System.out.print(s.asJson());
    }


    //5.查询应用信息列表
    @Test
    public void queryByConditions(){
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
        l.add(conditions);

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


//        Conditions conditions3 = new Conditions();
//        conditions3.setName("status");
//        conditions3.setOp("not in");
//        String test1 = "1";
//        String test2 = "2";
//        conditions3.setValue("(" + test1 + "," + test2 + ")");
//        l.add(conditions3);

        commonQueryParam.setConditions(l);

        PageDto<ApplicationInfoDto> pageDto= service.applicationQueryByConditions(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    //6.查询应用负载列表
    @Test
    public void queryLoad(){
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(10);
        commonQueryParam.setSortBy("application");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");

        List<Conditions> l = new ArrayList<>();

        Conditions conditions1 = new Conditions();
        conditions1.setName("application");
        conditions1.setOp("=");
        conditions1.setValue("9f81b9abc30244f0b02621f34ab9856a");
        l.add(conditions1);


        Conditions conditions = new Conditions();
        conditions.setName("__userTag__");
        conditions.setOp("is null");
        conditions.setValue("%D%");
        l.add(conditions);

        commonQueryParam.setConditions(l);

        PageDto<ApplicationInfoLoadDto> pageDto= service.applicationLoadQueryByConditions(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }



    /**
     * 7、操作负载
     *
     * start：启动
     * restart：重启
     * stop：停止
     * pause：暂停
     * unpause：解除暂停
     * delete：删除
     * kill  : 结束进程
     *
     *
     */
    @Test
    public void operationLoad() {
        ContainerOpParam p = new ContainerOpParam();
        p.setId("5babdb9b05e9442ee338c9787322803fc943c64b86a318069689f1103521d10c");
        p.setOp("delete");
        Status<String> status = service.operationLoad(p);
        System.out.println(status.asJson());
    }

    //8、添加负载
    @Test
    public void  addLoad(){
        ApplicationLoadAddParam  p=new ApplicationLoadAddParam();
        p.setAmount(4);
        p.setId("9f81b9abc30244f0b02621f34ab9856a");
        List<ApplicationInfoLoadDto>  l=service.addLoad(p);
        for(ApplicationInfoLoadDto a:l){
            System.out.println(a.asJson());
        }
    }


    //9、重新发布应用
    @Test
    public void  republish(){
        ApplicationInfoDto  a=service.republish("9f81b9abc30244f0b02621f34ab9856a");
    }

}
