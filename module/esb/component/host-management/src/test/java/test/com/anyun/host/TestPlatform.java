package test.com.anyun.host;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PlatformDto;
import com.anyun.cloud.param.PlatformCreateParam;
import com.anyun.cloud.param.PlatformSetDefaultParam;
import com.anyun.cloud.param.PlatformUpdateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.dao.PlatformDao;
import com.anyun.esb.component.host.dao.impl.PlatformDaoImpl;
import com.anyun.esb.component.host.service.publish.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/19.
 */
public class TestPlatform   extends BaseComponentTest{
    private PlatformDao platformDao;

    @Before
    public void setup() throws Exception {
       platformDao=new PlatformDaoImpl();
    }

    //设为默认平台
    @Test
    public void testPlatformSetAsDefaultService(){
        PlatformSetDefaultParam  param=new PlatformSetDefaultParam();
        param.setUserUniqueId("");
        param.setId("");
        PlatformSetAsDefaultService platformSetAsDefaultService=new PlatformSetAsDefaultService();
        exchange.getIn().setBody(param.asJson());
        Status<String>   status =(Status) platformSetAsDefaultService.process(endpoint,exchange);
        System.out.println(status.getStatus());
    }


    //创建平台
    @Test
    public void testPlatformCreateService(){
        PlatformCreateService platformCreateService =new PlatformCreateService();
        PlatformCreateParam   platformCreateParam =new PlatformCreateParam();
        platformCreateParam.setArea("");
        platformCreateParam.setName("");
        platformCreateParam.setDescription("");
        platformCreateParam.setIpDomain("anyun.com");
        platformCreateParam.setPort("8989");
        platformCreateParam.setBaseUrl("/rest/v1");
        platformCreateParam.setStatus(1);
        platformCreateParam.setUserUniqueId("");
        exchange.getIn().setBody(platformCreateParam.asJson());
        Status<String>   status =(Status)platformCreateService.process(endpoint,exchange);
        System.out.println(status.getStatus());
    }


    //修改平台
    @Test
    public void testPlatformUpdateService(){
        PlatformUpdateService platformUpdateService=new PlatformUpdateService();
        PlatformUpdateParam platformUpdateParam=new PlatformUpdateParam();
        platformUpdateParam .setId("6d9de3920e0c46078bb2b7a07a472ebb");
        platformUpdateParam.setArea("1");
        platformUpdateParam.setName("1");
        platformUpdateParam.setDescription("1");
        platformUpdateParam.setIpDomain("api1.esb.iaas.anyuncloud.com");
        platformUpdateParam.setPort("8989");
        platformUpdateParam.setBaseUrl("/rest/v1");
        platformUpdateParam.setStatus(1);
        platformUpdateParam.setUserUniqueId("");
        exchange.getIn().setBody(platformUpdateParam.asJson());
        Status<String>   status =(Status) platformUpdateService.process(endpoint,exchange);
        System.out.println(status.getStatus());
    }

    //根据id删除平台
    @Test
    public void testPlatformDeleteService(){
        PlatformDeleteService platformDeleteService=new PlatformDeleteService();
        Map<String,Object>   param =new HashMap();
        param.put("id","");
        param.put("userUniqueId","");
        exchange.getIn().setHeaders(param);
        Status<String> status= (Status)platformDeleteService.process(endpoint,exchange);
        System.out.println(status.getStatus());
    }


   //按id查询平台信息
    @Test
   public void tesrPlatformQueryById(){
        PlatformQueryByIdService platformQueryByIdService= new PlatformQueryByIdService();
        Map<String,Object>   param =new HashMap();
        param.put("id","");
        param.put("userUniqueId","");
        exchange.getIn().setHeaders(param);
        PlatformDto   platformDto=(PlatformDto) platformQueryByIdService.process(endpoint,exchange);
        System.out.println(platformDto.asJson());
    }

    //查询默认平台
    @Test
    public  void  testPlatformQueryDefaultService(){
        PlatformQueryDefaultService  platformQueryDefaultService=new PlatformQueryDefaultService();
        exchange.getIn().setHeader("userUniqueId","");
        PlatformDto   platformDto=(PlatformDto) platformQueryDefaultService.process(endpoint,exchange);
        System.out.println("response:"+platformDto.asJson());
    }

    //查询所有平台
    @Test
    public  void testPlatformQueryAllService(){
        PlatformQueryAllService   platformQueryAllService=new PlatformQueryAllService();
        exchange.getIn().setHeader("userUniqueId","");
        List<PlatformDto>  list=(List)platformQueryAllService.process(endpoint,exchange);
        System.out.println(JsonUtil.toJson(list));
    }
}
