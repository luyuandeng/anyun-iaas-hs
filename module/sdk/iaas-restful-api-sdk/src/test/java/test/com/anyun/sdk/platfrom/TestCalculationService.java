package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CalculationSchemeCreateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.CalculationSchemeService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twh-workspace on 17-11-23.
 */
public class TestCalculationService extends BaseAnyunTest {
    private CalculationSchemeService calculationSchemeService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        calculationSchemeService =  factory.getCalculationSchemeService();
        ResourceClient.setUserToken("debug_token");
    }

    //------------------------------------------测试--------------------------------------------

    /**
     * 根据id查询计算方案
     * @throws Exception
     */
    @Test
    public void TeseQueryById()throws Exception{
        String id="1";
        String userUniqueId="";
        CalculationSchemeDto calculationSchemeDto = calculationSchemeService.queryCalculationSchemeByid(id,userUniqueId);
        System.out.println(calculationSchemeDto.asJson());
    }

    @Test
    public void TeseQueryList()throws Exception{
        String userUniqueId="";
        List<CalculationSchemeDto> calculationSchemeDtos = calculationSchemeService.queryCalculationSchemeList(userUniqueId);
        System.out.println(JsonUtil.toJson(calculationSchemeDtos));
    }

    /**
     * 3,根据条件分页查询计算方案
     * @throws Exception
     */
    @Test
    public void   getPageListConditions()throws Exception {
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(3);
        commonQueryParam.setSortBy("");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("");

        List<Conditions> l = new ArrayList<>();

        Conditions conditions = new Conditions();
        conditions.setName("__userTag__");
        conditions.setOp("is null");
        conditions.setValue("%D%");
        l.add(conditions);


        Conditions conditions1 = new Conditions();
        conditions1.setName("name");
        conditions1.setOp("is not null");
        conditions1.setValue("%S%");
        l.add(conditions1);

        Conditions conditions2 = new Conditions();
        conditions2.setName("");
        conditions2.setOp("");
        conditions2.setValue("");
        l.add(conditions2);

        commonQueryParam.setConditions(l);

        PageDto<CalculationSchemeDto> pageDto= calculationSchemeService.queryCalculationSchemeDtoBycondition(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    /**
     * 4,删除计算方案
     */
    @Test
    public void TestDelete(){
        String id="4677f17a235a498a89f88d5c30d01f59";
        String userUniqueId="";
        Status<String> status = calculationSchemeService.deleteCalculationSchemeById(id,userUniqueId);
        System.out.println(status.getStatus());
    }

    /**
     * 5、创建计算方案
     */
    @Test
    public void TestCreate(){
        CalculationSchemeCreateParam param = new CalculationSchemeCreateParam();
        param.setDescription("dd");
        param.setName("dd");
        param.setCpuShares(4);
        param.setMemory(4);
        CalculationSchemeDto calculationSchemeDto = calculationSchemeService.createCalculationScheme(param);
        System.out.println(JsonUtil.toJson(calculationSchemeDto));
    }

}
