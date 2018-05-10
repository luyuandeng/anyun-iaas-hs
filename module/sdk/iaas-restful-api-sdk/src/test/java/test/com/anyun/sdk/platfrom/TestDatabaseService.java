package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DatabaseDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.DatabaseCreateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.DatabaseService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by sxt on 7/4/17.
 */
public class TestDatabaseService extends BaseAnyunTest {
    private DatabaseService service;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        service = factory.getDatabaseService();
        ResourceClient.setUserToken("debug_token");
    }


    /**
     * 1、获取数据库详情
     */
    @Test
    public void getDetails() {
        String id = "44b9074df3014e7ba926e5d5f32e4109";
        String userUniqueId = "";
        DatabaseDto databaseDto = service.getDetails(id, userUniqueId);
        System.out.println(databaseDto.asJson());
    }


    /**
     * 2、 分页查询数据库列表
     */
    @Test
    public void getPageListByCondition() {
        //        {name:“__userTag__",op:"in",value:"tag1,tag2"}
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
        commonQueryParam.setSortBy("name");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");

//        List<Conditions> l = new ArrayList<>();
//
//        Conditions conditions = new Conditions();
//        conditions.setName("__userTag__");
//        conditions.setOp("is null");
//        conditions.setValue("%D%");
//        l.add(conditions);

//        Conditions conditions1 = new Conditions();
//        conditions1.setName("status");
//        conditions1.setOp("like");
//        conditions1.setValue("%2%");
//        l.add(conditions1);

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

//        commonQueryParam.setConditions(l);
        CommonQueryParam param = new CommonQueryParam();
        PageDto<DatabaseDto> pageDto = service.getPageListByCondition(param);
        System.out.println(JsonUtil.toJson(pageDto));
    }


    /**
     * 3、 删除数据库
     */
    @Test
    public void delete() {
        String id = "44b9074df3014e7ba926e5d5f32e4109";
        String userUniqueId = "";
        Status<String> status = service.delete(id, userUniqueId);
        System.out.println(status.getStatus());
    }


    /**
     * 4、创建数据库
     */
    @Test
    public void create() {
        DatabaseCreateParam param = new DatabaseCreateParam();
        param.setUserUniqueId("sxt");
        param.setProjectId("4df6c2f3ae964db88ada5567e805fa3d");
        param.setName("cluster");
        param.setDescribe("cluster");
        param.setBridgeL3("anyun-in");
        param.setBridgeL3Ip("192.168.1.251");
        param.setType("cluster");
        param.setSqldNodeTotal(8);
        param.setUserName("anyun");
        param.setPassword("1234qwer");
        DatabaseDto databaseDto = service.create(param);
        System.out.println(databaseDto.asJson());
    }
}
