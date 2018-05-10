package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.NetL2InfoDto;
import com.anyun.cloud.param.NetL2CreateParam;
import com.anyun.cloud.param.NetL2UpdateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.NetL2Service;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by sxt on 17-4-10.
 */
public class TestNetL2 extends BaseAnyunTest {
    private NetL2Service service;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        service = factory.getNetL2Service();
        ResourceClient.setUserToken("debug_token");
    }


    //1、查询 L2网络详情
    @Test
    public void getDetails() {
        String userUniqueId = "";
        String id = "d980240bf62241019304ad8c5cb6b4d5";
        NetL2InfoDto netL2InfoDto = service.getDetails(id, userUniqueId);
        System.out.print("netL2InfoDto:" + JsonUtil.toJson(netL2InfoDto));
    }

    //2、查询 L2网络列表
    @Test
    public void getNetL2InfoList() {
        String userUniqueId = "";
        String type = "DOCKER";
        List<NetL2InfoDto> l = service.getNetL2InfoList(type, userUniqueId);
        System.out.print("netL2InfoDto:" + JsonUtil.toJson(l));
    }

    //3.删除
    @Test
    public void deleteNetL2(){
        String id = "2";
        String userUniqueId = "";
        Status<String> status = service.netL2DeleteById(id,userUniqueId);
        System.out.println(status.getStatus());
    }

    //4.添加
    @Test
    public void createNetL2(){
        String userUniqueId = "";
        NetL2CreateParam param = new NetL2CreateParam();
        param.setName("3");
        param.setDescription("3");
        param.setCluster("3");
        param.setPhysical_interface("3");
        param.setType("DOCKER");
        NetL2InfoDto dto = service.createNetL2(param,userUniqueId);
        NetL2InfoDto netL2InfoDto = service.getDetails(dto.getId(), userUniqueId);
        System.out.println("netL2InfoDto:" + JsonUtil.toJson(netL2InfoDto));
    }

    //5.修改
    @Test
    public void updateNetL2(){
        String userUniqueId = "";
        NetL2UpdateParam param = new NetL2UpdateParam();
        param.setId("dc77ee238774485bb5ecfe7ca4ff9406");
        param.setName("555");
        param.setPhysical_interface("555");
        param.setCluster("555");
        NetL2InfoDto dto = service.updateNetL2(param,userUniqueId);
        NetL2InfoDto netL2InfoDto = service.getDetails(dto.getId(), userUniqueId);
        System.out.println("netL2InfoDto:" + JsonUtil.toJson(netL2InfoDto));
    }
}
