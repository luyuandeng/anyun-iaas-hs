package test.com.anyun.host;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.SecurityGroupDto;
import com.anyun.cloud.param.AddIpToSecurityGroupParam;
import com.anyun.cloud.param.RemoveIpFromSecurityGroupParam;
import com.anyun.cloud.param.SecurityGroupCreateParam;
import com.anyun.cloud.param.SecurityGroupUpdateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.service.publish.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-10-17.
 */
public class TestSecurityGroup extends BaseComponentTest {
    private SecurityGroupCreateService securityGroupCreateService;
    private SecurityGroupUpdateService securityGroupUpdateService;
    private SecurityGroupQueryByLabelService  securityGroupQueryByLabelService;
    private SecurityGroupQueryByProjectService securityGroupQueryByProjectService;
    private SecurityGroupDeleteByLabelService securityGroupDeleteByLabelService;
    private AddIpToSecurityGroupService addIpToSecurityGroupService;
    private RemoveIpFromSecurityGroupService removeIpFromSecurityGroupService;

    @Before
    public void setUp() throws Exception {
        securityGroupCreateService = new SecurityGroupCreateService();
        securityGroupUpdateService= new SecurityGroupUpdateService();
        securityGroupQueryByLabelService=new SecurityGroupQueryByLabelService();
        securityGroupQueryByProjectService=new SecurityGroupQueryByProjectService();
        securityGroupDeleteByLabelService=new SecurityGroupDeleteByLabelService();
        addIpToSecurityGroupService=new AddIpToSecurityGroupService();
        removeIpFromSecurityGroupService=new RemoveIpFromSecurityGroupService();
    }

    @Test
    public void testSecurityGroupCreateService(){
        SecurityGroupCreateParam param = new SecurityGroupCreateParam();
        param.setProject("97eed958b472488dad188bfdc993c768");
        param.setName("安全组");
        param.setDescription("这是一个创建安全组的测试");
        param.setIpOrSegment("192.168.1.1");
        param.setPort("8088");
        /**
         *    ALLOW_ACCESS
         *    ALLOW_IN
         *    ALLOW_OUT
         */
        param.setRule("ALLOW_ACCESS");
        param.setUserUniqueId("");
        exchange.getIn().setBody(param.asJson());
        Status<String> status = (Status<String>) securityGroupCreateService.process(endpoint, exchange);
        System.out.println("status:" + status.getStatus());
    }


    @Test
    public void testSecurityGroupUpdateService(){
        SecurityGroupUpdateParam  param= new SecurityGroupUpdateParam();
        param.setLabel("anyun-WrDJwOOlXn");
        param.setName("");
        param.setDescription("");
        param.setUserUniqueId("");
        exchange.getIn().setBody(param.asJson());
        Status<String> status = (Status<String>) securityGroupUpdateService.process(endpoint,exchange);
        System.out.println("status:" + status.getStatus());
    }

    @Test
    public void testSecurityGroupQueryByLabelService(){
        Map<String,Object>  param=new HashMap<>();
        param.put("label","");
        param.put("userUniqueId","");
        exchange.getIn().setHeaders(param);
        SecurityGroupDto  dto= (SecurityGroupDto) securityGroupQueryByLabelService.process(endpoint,exchange);
        System.out.println(dto.asJson());
    }

    @Test
    public void testSecurityGroupQueryByProjectService(){
        Map<String,Object>  param=new HashMap<>();
        param.put("project","");
        param.put("userUniqueId","");
        exchange.getIn().setHeaders(param);
        List<SecurityGroupDto> list = (List<SecurityGroupDto>) securityGroupQueryByProjectService.process(endpoint,exchange);
        System.out.println("list:"+ JsonUtil.toJson(list));
    }

    @Test
    public void testSecurityGroupDeleteByLabelService(){
        Map<String,Object>  param=new HashMap<>();
        param.put("label","");
        param.put("userUniqueId","");
        Status<String>   status=(Status)securityGroupDeleteByLabelService.process(endpoint,exchange);
        System.out.println(status.getStatus());
    }

    @Test
    public void testAddIpToSecurityGroupService(){
        AddIpToSecurityGroupParam  param=new AddIpToSecurityGroupParam();
        param.setSecurityGroupId("das");
        param.setContainerNetIpId("sd");
        param.setUserUniqueId("");
        exchange.getIn().setBody(param.asJson());
        Status<String> status=(Status)addIpToSecurityGroupService.process(endpoint,exchange);
        System.out.println(status.getStatus());
    }

    @Test
    public void testRemoveIpFromSecurityGroupService(){
        RemoveIpFromSecurityGroupParam  param=new RemoveIpFromSecurityGroupParam();
        param.setContainerNetIpId("sd");
        param.setSecurityGroupId("sd");
        param.setUserUniqueId("");
        exchange.getIn().setBody(param.asJson());
        Status<String> status=(Status)removeIpFromSecurityGroupService.process(endpoint,exchange);
        System.out.println(status.getStatus());
    }
}
