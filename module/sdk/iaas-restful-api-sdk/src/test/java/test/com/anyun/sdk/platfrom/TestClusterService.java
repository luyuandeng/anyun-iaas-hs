package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ClusterDto;
import com.anyun.cloud.param.ClusterChangeStatusParam;
import com.anyun.cloud.param.ClusterCreateParam;
import com.anyun.cloud.param.ClusterUpdateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.ClusterService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by jt-workspace on 17-4-6.
 */
public class TestClusterService extends BaseAnyunTest {
    private ClusterService service;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        service = factory.getClusterService();
        ResourceClient.setUserToken("debug_token");
    }

    //1、查询集群详情
    @Test
    public void TestClusterQueryById() throws Exception {
        String id = "a9b1157f0545465e8a17904ef0bea426";
        String userUniqueId = "";
        ClusterDto clusterDto = service.ClusterQueryById(id, userUniqueId);
        System.out.println(JsonUtil.toJson(clusterDto));
    }

    //2、查询集群列表
    @Test
    public void TestClusterQueryList() throws Exception {
        //  Enable Disable  All
        String status = "All";
        String userUniqueId = "";
        List<ClusterDto> clusterDto = service.ClusterQueryListService(status, userUniqueId);
        System.out.println(JsonUtil.toJson(clusterDto));
    }

    //3、删除集群
    @Test
    public void TestDelete() {
        String id = "a9b1157f0545465e8a17904ef0bea426";
        String userUniqueId = "";
        Status<String> status = service.ClusterDeleteService(id, userUniqueId);
        System.out.println(status.getStatus());
    }

    //4、创建集群
    @Test
    public void TestClusterCreate() throws Exception {
        ClusterCreateParam param = new ClusterCreateParam();
        param.setName("t");
        param.setDescription("create cluster test");
        param.setStatus("Enable");
        param.setUserUniqueId("");
        ClusterDto clusterDto = service.ClusterCreateService(param);
        System.out.print(JsonUtil.toJson(clusterDto));
    }


    //5、修改集群
    @Test
    public void TestUpdate() {
        ClusterUpdateParam param = new ClusterUpdateParam();
        param.setId("fb32a7eaca3a42fda9b180c9573d996b");
        param.setName("test");
        param.setDescription("create cluster test");
        param.setStatus("Disable");
        param.setUserUniqueId("");
        ClusterDto clusterDto = service.ClusterUpdateService(param);
        System.out.print(JsonUtil.toJson(clusterDto));
    }

    // 6、修改集群状态
    @Test
    public void TestChange() {
        ClusterChangeStatusParam param = new ClusterChangeStatusParam();
        param.setId("fb32a7eaca3a42fda9b180c9573d996b");
        param.setStatus("Enable");
        param.setUserUniqueId("");
        Status<String> status = service.changeStatus(param);
        System.out.print(status.getStatus());
    }
}
