package test.com.anyun.host;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.param.DiskSchemeCreateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.dao.DiskSchemeDao;
import com.anyun.esb.component.host.dao.impl.DiskSchemeDaoImpl;
import com.anyun.esb.component.host.service.docker.DiskSchemeService;
import com.anyun.esb.component.host.service.docker.impl.DiskSchemeServiceImpl;
import com.anyun.esb.component.host.service.publish.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jt on 17-11-22.
 */
public class testDiskScheme extends BaseComponentTest  {

    private DiskSchemeService diskSchemeService;
    private DiskSchemeCreateService diskSchemeCreateService;
    private DiskSchemeDeleteByIdService diskSchemeDeleteByIdService;
    private DiskSchemeListQueryService diskSchemeListQueryService;
    private DiskSchemeQueryByConditionsService diskSchemeQueryByConditionsService;
    private DiskSchemeQueryByIdService diskSchemeQueryByIdService;

    @Before
    public void setup() throws Exception {

        diskSchemeService = new DiskSchemeServiceImpl();
        diskSchemeCreateService =new DiskSchemeCreateService();
        diskSchemeDeleteByIdService =new DiskSchemeDeleteByIdService();
        diskSchemeListQueryService = new DiskSchemeListQueryService();
        diskSchemeQueryByConditionsService =new DiskSchemeQueryByConditionsService();
        diskSchemeQueryByIdService = new DiskSchemeQueryByIdService();

    }


    @Test
    public void TestDiskSchemeCreateService(){
        DiskSchemeCreateParam diskSchemeCreateParam =new DiskSchemeCreateParam();
        diskSchemeCreateParam.setSize(20);
        diskSchemeCreateParam.setName("1312");
        diskSchemeCreateParam.setDescription("321");
        exchange.getIn().setBody(diskSchemeCreateParam.asJson());
        System.out.println(diskSchemeCreateService.process(endpoint,exchange));

    }


    @Test
    public void TestDiskSchemeDeleteByIdService(){

        Map<String,Object> param =new HashMap();
        param.put("id","21312");
        param.put("userUniqueId","");
        exchange.getIn().setHeaders(param);
        Status<String> status= (Status)diskSchemeDeleteByIdService.process(endpoint,exchange);
        System.out.println(status.getStatus());

    }
    @Test
    public void TestDiskSchemeListQueryService(){


        Map<String,Object> param =new HashMap();
        param.put("userUniqueId","21");
        exchange.getIn().setHeaders(param);
        diskSchemeListQueryService.process(endpoint,exchange);
        System.out.println(diskSchemeListQueryService.process(endpoint,exchange));
    }


    @Test
    public void TestDiskSchemeQueryByConditionsService(){

        //{name:“__userTag__",op:"in",value:"tag1,tag2"}
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
//        l.add(conditions);

        Conditions conditions1 = new Conditions();
        conditions1.setName("type");
        conditions1.setOp("like");
        conditions1.setValue("%D%");
//        l.add(conditions1);

        Conditions conditions2 = new Conditions();
        conditions2.setName("name");
        conditions2.setOp("is not null");
        conditions2.setValue("1");
//        l.add(conditions2);


        Conditions conditions4 =new Conditions();
        conditions4.setName("id");
        conditions4.setOp("like");
        conditions4.setValue("0bde17f79d954c5e9d541435ae5e9ea3");
        l.add(conditions4);


        Conditions conditions3 = new Conditions();
        conditions3.setName("id");
        conditions3.setOp("like");
        String test1 = "0bde17f79d954c5e9d541435ae5e9ea3";
        String test2 =  "b96ee9063352412dbbebe3b57dba52b0";
        String test3 = "20fe287bf24640cc87c8aadbbc2a739a";
        conditions3.setValue("(" + test1 + "," + test2 +"," + test3+")");
//        l.add(conditions3);

        commonQueryParam.setConditions(l);
        commonQueryParam.setUserUniqueId("dsadas");
//        exchange.getIn().setHeader("s", commonQueryParam.asJson());
//        PageDto<DiskSchemeDto> pageDto = (PageDto<DiskSchemeDto>) diskSchemeQueryByConditionsService.process(endpoint, exchange);
//        System.out.println(JsonUtil.toJson(pageDto));
        commonQueryParam.setConditions(l);
        exchange.getIn().setHeader("s", commonQueryParam.asJson());
        PageDto<DiskSchemeDto> pageDto = (PageDto<DiskSchemeDto>) diskSchemeQueryByConditionsService.process(endpoint, exchange);
        System.out.println(JsonUtil.toJson(pageDto));

    }
    @Test
    public void TestDiskSchemeQueryByIdService(){


        Map<String,Object> param =new HashMap();
        param.put("id","6dde88b6555a4d0eb0c9214eb7591c8b");
        param.put("userUniqueId","");
        exchange.getIn().setHeaders(param);

        System.out.println(       diskSchemeQueryByIdService.process(endpoint,exchange));
    }

}
