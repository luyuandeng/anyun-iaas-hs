package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PlatformDto;
import com.anyun.cloud.param.PlatformCreateParam;
import com.anyun.cloud.param.PlatformSetDefaultParam;
import com.anyun.cloud.param.PlatformUpdateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.PlatformService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by sxt on 16-10-21.
 */
public class TestPlatform extends BaseAnyunTest {
    private PlatformService platformService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        platformService = factory.getPlatformService();
        ResourceClient.setUserToken("debug_token");
    }


    @Test
    public void testPlatformCreate() {
        PlatformCreateParam platformCreateParam = new PlatformCreateParam();
        platformCreateParam.setArea("");
        platformCreateParam.setName("");
        platformCreateParam.setDescription("");
        platformCreateParam.setIpDomain("api1.esb.iaas.anyuncloud.com");
        platformCreateParam.setPort("8989");
        platformCreateParam.setBaseUrl("/rest/v1");
        platformCreateParam.setStatus(0);
        platformCreateParam.setUserUniqueId("");
        Status<String> status = platformService.platformCreate(platformCreateParam);
        System.out.println(status.getStatus());
    }

    @Test
    public void testPlatformUpdate() {
        PlatformUpdateParam platformUpdateParam = new PlatformUpdateParam();
        platformUpdateParam.setId("9c52089eeac54aed8631e534497206c2");
        platformUpdateParam.setArea("1");
        platformUpdateParam.setName("1");
        platformUpdateParam.setDescription("1");
        platformUpdateParam.setIpDomain("api1.esb.iaas.anyuncloud.com");
        platformUpdateParam.setPort("8989");
        platformUpdateParam.setBaseUrl("/rest/v1");
        platformUpdateParam.setStatus(1);
        platformUpdateParam.setUserUniqueId("");
        Status<String> status = platformService.platformUpdate(platformUpdateParam);
        System.out.println(status.getStatus());
    }

    @Test
    public void testPlatformSetAsDefault() {
        PlatformSetDefaultParam param=new PlatformSetDefaultParam();
        param.setUserUniqueId("");
        param.setId("0a1b6a0c9bcc4bcf959035232315efbe");
        Status<String> status = platformService.platformSetAsDefault(param);
        System.out.println(status.getStatus());
    }

    @Test
    public void testPlatformDelete() {
        String id="9c52089eeac54aed8631e534497206c2";
        String userUniqueId="";
        Status<String> status = platformService.platformDelete(id,userUniqueId);
        System.out.println(status.getStatus());
    }

    @Test
    public void testPlatformQueryById() {
        String id="9c52089eeac54aed8631e534497206c2";
        String userUniqueId="";
        PlatformDto platformDto = platformService.platformQueryById(id,userUniqueId);
        System.out.println(platformDto.asJson());
    }

    @Test
    public void testPlatformQueryDefault() {
        String userUniqueId="";
        PlatformDto platformDto = platformService.platformQueryDefault(userUniqueId);
        System.out.println(platformDto.asJson());
    }

    @Test
    public void testPlatformQueryAll() {
        String userUniqueId="";
        List<PlatformDto> platformDtoList = platformService.platformQueryAll(userUniqueId);
        System.out.println(JsonUtil.toJson(platformDtoList));
    }

    @Test
    public void testLogicCenterQuery(){
        //   QUERY_BY_ID
        //   QUERY_A_DEFAULT
        //   QUERY_ALL
        String subMethod ="QUERY_ALL";
        String subParameters="";
        List<PlatformDto>  l = platformService.logicCenterQueryQuery("",subMethod,subParameters);
    }
}
