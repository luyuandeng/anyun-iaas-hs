package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.SecurityGroupDto;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.SecurityGroupService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-10-19.
 */
public class TestSecurityGroup extends BaseAnyunTest {
    private SecurityGroupService securityGroupService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        securityGroupService = factory.getSecurityGroupService();
        ResourceClient.setUserToken("debug_token");
    }

    //1、查询安全组详情
    @Test
    public void getDetails() {
        String label = "anyun-ypzr0AW0K78";
        String userUniqueId="";
        SecurityGroupDto securityGroupDto = securityGroupService.securityGroupQueryByLabel(label,userUniqueId);
        System.out.println("response:" + securityGroupDto.asJson());
    }

    //2、根据项目查询安全组列表
    @Test
    public void getList() {
        String project = "b0cc3c8a76804e639074bf38f93bc0b3";
        String userUniqueId="";
        List<SecurityGroupDto> l = securityGroupService.securityGroupQueryByProject(project,userUniqueId);
        for(SecurityGroupDto dto:l){
            System.out.println(dto.asJson());
        }
    }

    //3、删除 安全组
    @Test
    public void deleteSecurityGroup() {
        String label = "anyun-5eEPYdJbwP2";
        String userUniqueId="";
        Status<String> status = securityGroupService.securityGroupDeleteByLabel(label,userUniqueId);
        System.out.println("response:" + status.getStatus());
    }

    //4、创建安全组
    @Test
    public void createSecurityGroup() {
        String userUniqueId = "";
        SecurityGroupCreateParam param = new SecurityGroupCreateParam();
        param.setProject("b0cc3c8a76804e639074bf38f93bc0b3");
        param.setName("demo");
        param.setDescription("");
        param.setPort("80");
        param.setRule("ALLOW_ACCESS");
        param.setIpOrSegment("192.168.1.1");
        param.setUserUniqueId("");
        SecurityGroupDto status = securityGroupService.securityGroupCreate(param);
        SecurityGroupDto dto = securityGroupService.securityGroupQueryByLabel(status.getLabel(),userUniqueId);
        System.out.println(dto.asJson());
    }

    //5、修改安全组
    @Test
    public void updateSecurityGroup() {
        String userUniqueId = "";
        SecurityGroupUpdateParam param = new SecurityGroupUpdateParam();
        param.setLabel("anyun-1mYkb3J77nj");
        param.setName("66666");
        param.setDescription("66666");
        param.setUserUniqueId("66666");
        SecurityGroupDto status = securityGroupService.securityGroupUpdate(param);
        SecurityGroupDto dto = securityGroupService.securityGroupQueryByLabel(status.getLabel(),userUniqueId);
        System.out.println("dto:" + JsonUtil.toJson(dto));
    }

    //6、添加 ip 到安全组
    @Test
    public void addIpToSecurityGroup() {
        AddIpToSecurityGroupParam param = new AddIpToSecurityGroupParam();
        param.setContainerNetIpId("06f673c4b2d34d49accc5c3e1ce95130");
        param.setSecurityGroupId("anyun-ypzr0AW0K78");
        param.setUserUniqueId("");
        Status<String> status = securityGroupService.addIpToSecurityGroup(param);
        System.out.println("response:" + status.getStatus());
    }

    //6、移除 ip 从安全组移除
    @Test
    public void removeIpFromSecurityGroup() {
        RemoveIpFromSecurityGroupParam param = new RemoveIpFromSecurityGroupParam();
        param.setContainerNetIpId("e824285b1611406aa4aea1bf37567928");
        param.setSecurityGroupId("anyun-ypzr0AW0K78");
        param.setUserUniqueId("");
        Status<String> status = securityGroupService.removeIpFromSecurityGroup(param);
        System.out.println("response:" + status.getStatus());
    }

    /**
     * 7、根据条件查询安全组
     */
    @Test
    public void   getPageListConditions()throws Exception{
//        {name:“__userTag__",op:"in",value:"tag1,tag2"}
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(3);
        commonQueryParam.setSortBy("name");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");
//
//        List<Conditions> l = new ArrayList<>();
//
//        Conditions conditions = new Conditions();
//        conditions.setName("__userTag__");
//        conditions.setOp("is null");
//        conditions.setValue("%D%");
//        l.add(conditions);
////
//        Conditions conditions1 = new Conditions();
//        conditions1.setName("port");
//        conditions1.setOp("like");
//        conditions1.setValue("%1%");
//        l.add(conditions1);
//
//        Conditions conditions2 = new Conditions();
//        conditions2.setName("name");
//        conditions2.setOp("is not null");
//        conditions2.setValue("11");
//        l.add(conditions2);


//        Conditions conditions3 = new Conditions();
//        conditions3.setName("name");
//        conditions3.setOp("in");
//        String test1 = "111111";
//        String test2 = "14141";
//        conditions3.setValue("(" + test1 + "," + test2 + ")");
//        l.add(conditions3);
//
//        commonQueryParam.setConditions(l);


        PageDto<SecurityGroupDto> pageDto= securityGroupService.getPageListConditions(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    //8、查询安全组列表
    @Test
    public   void  testQuerySecurityGroup(){
        //QUERY_BY_PROJECTID
        //QUERY_BY_CONTAINERID
        String userUniqueId="";
        String subMethod="QUERY_BY_CONTAINERID";
        String subParameters="573776b184236511c050d3d433da02483c0a54853949cb3e2661a4fff5255631";
        List<SecurityGroupDto>  l=securityGroupService.querySecurityGroup(userUniqueId,subMethod,subParameters);
    }
}
