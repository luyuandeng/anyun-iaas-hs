package test.com.anyun.host;

import com.anyun.cloud.dto.AreaDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.service.publish.AreaQueryByConditionsService;
import com.anyun.esb.component.host.service.publish.AreaQueryByTypeOrStatusService;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created by twh-workspace on 17-3-27.
 */
public class TestArea extends BaseComponentTest {

    private AreaQueryByTypeOrStatusService areaQueryByTypeOrStatusService;
    private AreaQueryByConditionsService areaQueryByConditionsService;

    @Before
    public void setup() throws Exception {
        areaQueryByTypeOrStatusService = new AreaQueryByTypeOrStatusService();
        areaQueryByConditionsService = new AreaQueryByConditionsService();
    }

    @Test
    public void testAreaCreateService() {
        Map<String, Object> m = new HashMap();
        m.put("id", "");
        m.put("name", "");
        m.put("description", "");
        m.put("type", "KVM");
        exchange.getIn().setHeaders(m);

    }

    @Test
    public void testAreaQueryByTypeOrStatusService() {
        Map<String, Object> m = new HashMap();
        m.put("userUniqueId", "");
        m.put("type", "");
        m.put("status", "");
        exchange.getIn().setHeaders(m);
        List<AreaDto> dtos = (List<AreaDto>) areaQueryByTypeOrStatusService.process(endpoint, exchange);
        System.out.println(JsonUtil.toJson(dtos));
    }

    @Test
    public void testAreaQueryByConditions() {
        //{name:“__userTag__",op:"in",value:"tag1,tag2"}
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
        commonQueryParam.setSortBy("name");
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
        conditions2.setValue("11");
//        l.add(conditions2);


        Conditions conditions3 = new Conditions();
        conditions3.setName("name");
        conditions3.setOp("not in");
        String test1 = "";
        String test2 =  "";
        String test3 = "";
        conditions3.setValue("(" + test1 + "," + test2 +"," + test3+")");
        l.add(conditions3);

        commonQueryParam.setConditions(l);
        exchange.getIn().setHeader("s", commonQueryParam.asJson());
        PageDto<AreaDto> pageDto = (PageDto<AreaDto>) areaQueryByConditionsService.process(endpoint, exchange);
        System.out.println(JsonUtil.toJson(pageDto));
    }
}
