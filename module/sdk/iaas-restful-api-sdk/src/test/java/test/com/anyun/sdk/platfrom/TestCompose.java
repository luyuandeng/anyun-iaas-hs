package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.ComposeCreateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by sxt on 16-12-8.
 */
public class TestCompose extends BaseAnyunTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(TestCompose.class);
    private static final String PATH_COMPOSE_CREATE = "/compose/create";
    private static final String PATH_COMPOSE_QUERY_BY_USERuNIQUEID="/compose/queryByUserUniqueId";
    private static final String PATH_COMPOSE_QUERY_BY_ID="/compose/queryById";
    private static final String PATH_COMPOSE_DELETE_BY_ID="/compose/delete";

    private static String getTemplate(String userUniqueId) {
        TemplateCompose t = new TemplateCompose();

        /****容器*********************************************************************/
        List<TemplateContainer> templateContainers = new ArrayList<>();
        TemplateContainer c = new TemplateContainer();
        c.setUserUniqueId(userUniqueId);
        c.setImageId("45028571E6EE583AFB47BF62020C92CA");
        c.setProjectId("");
        c.setPurpose("编排测试");
        c.setName("name");
        c.setCpuCoreLimit(1);
        c.setCpuFamily("Intel(R) Core(TM) i5-4460 CPU @ 3.20GHz");
        c.setHost("B48925F9F8970E48432206056A3808A6");
        c.setIp("192.168.1.155");
        c.setHostName("She-1111");
        c.setKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8nnMlna53qpCThGsp2wazb3p98bdnseis00WXy30Ned0H9H2OmWoIHiHYVbUtQhWjXTOHNAWB34mb4+uQ++9Z1IWOOZT3BjTF0ENMIrVhqRAVNqwq4IT3Z/o632vE3IStZKJMg55CN17mr6WRZodTplOt4G54bzN1WBnz6b00CSWGUMoBZJBwFjyUGgZ9KIAF7bQRKRxT3HU0PWyfKRwbZJSceinQyLPgtv0VJ66Y5dXDia3hknOx7iiIjGiSSIjtNN/7TBwL6ydM2eHyxaszrNZ9X9knjy7t9EWDkhZoMMSxDf29jrPFlylYkzrabUsrsQqyEtaJrs1vmxliqeVr");
        c.setUser("root");
        c.setMemoryTotal(15);
        c.setMemoryUnused(8);
        c.setMemoryLimit(1);
        c.setMemorySwapLimit(2);
        c.setPrivileged(0);
        c.setSecurityOpt(new HashMap<>());
        c.setUlimit(new HashMap<>());
        c.setCapAdd(new HashMap<>());
        templateContainers.add(c);
        t.setTemplateContainers(templateContainers);

        /****卷*********************************************************************/
//        List<TemplateVolume> templateVolumes = new ArrayList<>();
//        TemplateVolume v = new TemplateVolume();
//        v.setUserUniqueId(userUniqueId);
//        v.setProject("");
//        v.setName("编排");
//        v.setDescr("编排测试");
//        v.setSpace(1);
//        templateVolumes.add(v);
//        t.setTemplateVolumes(templateVolumes);

        /****网络 TODO*********************************************************************/

        return t.asJson();
    }


    //创建 项目编排  OK
    @Test
    public void testCreateCompose() {
        ComposeCreateParam createParam = new ComposeCreateParam();
        createParam.setUserUniqueId("2");
        createParam.setName("项目编排");
        createParam.setDescript("这是项目编排的一个测试");
        createParam.setSpace(10);
        createParam.setVersion("1.0.0");
        createParam.setTemplate(getTemplate(createParam.getUserUniqueId()));
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String  json=createParam.asJson();
        LOGGER.debug("sendJson:[{}]",json);
        String response = rsClient.put(PATH_COMPOSE_CREATE, json);
        System.out.println("response:" + response);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        LOGGER.debug(requests.getStatus());
    }


    //查询用户的编排  OK
    @Test
    public void testComposeQueryByUserUniqueId() {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String,Object>  param=new HashMap<>();
        param.put("userUniqueId","2");
        LOGGER.debug("sendParam:[{}]",param);
        String response = rsClient.query(PATH_COMPOSE_QUERY_BY_USERuNIQUEID, param);
        LOGGER.debug("response:[{}]",response);
        List<ComposeDto>  list= ResourceClient.convertToListObject(ComposeDto.class, response);
        LOGGER.debug(JsonUtil.toJson(list));
    }


    //根据id 查询 编排
    @Test
    public void testComposeQueryById() {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String,Object>  param=new HashMap<>();
        param.put("id","2fafe975fcc94d48b24660f3e5af0f4f");
        param.put("userUniqueId","huangwentao");

        LOGGER.debug("id:[{}]",param);
        String response = rsClient.query(PATH_COMPOSE_QUERY_BY_ID, param);
        LOGGER.debug("response:[{}]",response);
        ComposeDto composeDto= ResourceClient.convertToAnyunEntity(ComposeDto.class, response);
        LOGGER.debug(composeDto.asJson());
    }

    //根据id删除编排
    @Test
    public void testComposeDeleteById() {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String,Object>  param=new HashMap<>();
        param.put("id","2fafe975fcc94d48b24660f3e5af0f4f");
        param.put("userUniqueId","huangwentao");
        LOGGER.debug("id:[{}]",param);
        String response = rsClient.delete(PATH_COMPOSE_DELETE_BY_ID, param);
        LOGGER.debug("response:[{}]",response);
        Status<String> status= ResourceClient.convertToAnyunEntity(Status.class, response);
        LOGGER.debug(status.getStatus());

    }

    @Test
    public void   testOther(){
        Map<String,Object>   params=new HashMap<>();
        params.put("name","jack");
        params.put("age","10");
        params.put("phone","187930547650");
        LOGGER.debug("params:[{}]",JsonUtil.toJson(params));
        LOGGER.debug("----------------------------------------------------");
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//                 LOGGER.debug("key:[{}]------values:[{}]", entry.getKey(),entry.getValue()) ;
//        }

        for(String    key:params.keySet()){
             LOGGER.debug( "key:[{}],value:[{}]",key,params.get(key).toString());
        }
    }

}
