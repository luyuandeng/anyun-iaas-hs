package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.dto.ServiceOperationDto;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.ServiceOperationService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by gp on 16-12-15.
 */
public class TestServiceOperationService extends BaseAnyunTest {
    private ServiceOperationService serviceOperationService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        serviceOperationService = factory.getServiceOperationService();
        ResourceClient.setUserToken("debug_token");
    }

    //1、查询所有service服务列表
    @Test
    public void  getAllList(){
        String userUniqueId ="2";
        List<ServiceOperationDto> list = serviceOperationService.queryServiceOperationStatus(userUniqueId);
        for(ServiceOperationDto s:list){
            System.out.println(s.asJson());
        }
    }
}
