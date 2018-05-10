package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.param.DiskSchemeCreateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.DiskSchemeService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twh-workspace on 17-11-23.
 */
public class TestDiskSchemeService extends BaseAnyunTest {
    private DiskSchemeService diskSchemeService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        diskSchemeService = factory.getDiskSchemeService();
        ResourceClient.setUserToken("debug_token");
    }

    //------------------------------------------测试区域-------------------------------------------------

    /**
     * 根据id查询磁盘方案
     * @throws Exception
     */
    @Test
    public void TestQueryById()throws Exception{
        String id="1";
        String userUniqueId="";
        DiskSchemeDto diskSchemeDto = diskSchemeService.queryDiskSchemeByid(id,userUniqueId);
        System.out.println(diskSchemeDto.asJson());
    }

    @Test
    public void TestQueryList()throws Exception{
        String userUniqueId="";
        List<DiskSchemeDto> diskSchemeDtos = diskSchemeService.queryDiskSchemeList(userUniqueId);
        System.out.println(JsonUtil.toJson(diskSchemeDtos));
    }

    /**
     * 3,根据条件分页查询磁盘方案
     * @throws Exception
     */
    @Test
    public void   getPageListConditions()throws Exception {
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
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
        conditions1.setName("deviceCategory");
        conditions1.setOp("is not null");
        conditions1.setValue("%S%");
        l.add(conditions1);

        Conditions conditions2 = new Conditions();
        conditions2.setName("");
        conditions2.setOp("");
        conditions2.setValue("");
        l.add(conditions2);

        commonQueryParam.setConditions(l);

        PageDto<DiskSchemeDto> pageDto= diskSchemeService.queryDiskSchemeDtoBycondition(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    /**
     * 4,删除磁盘方案
     */
    @Test
    public void TestDelete(){
        String id="110ecb5fefe3ed4dsds721b6b2da635aa12117";
        String userUniqueId="";
        Status<String> status = diskSchemeService.deleteDiskSchemeById(id,userUniqueId);
        System.out.println(status.getStatus());
    }

    /**
     * 创建磁盘方案
     */
    @Test
    public void TestCreate(){
        DiskSchemeCreateParam param = new DiskSchemeCreateParam();
        param.setName("3");
        param.setDescription("3");
        param.setSize(20);
        DiskSchemeDto diskSchemeDto = diskSchemeService.createDiskSchemeDto(param);
        System.out.println(JsonUtil.toJson(diskSchemeDto));
    }

}
