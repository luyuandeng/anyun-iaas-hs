package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.PictureDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.PictureService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-10-18.
 */
public class TestPicture extends  BaseAnyunTest {
    private PictureService pictureService;
    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        pictureService = factory.getPictureService();
        ResourceClient.setUserToken("debug_token");
    }

    //1、查询所有图片列表
    @Test
    public void getAllList(){
        List<PictureDto> pictureDto =  pictureService.pictureQueryAll("huangwentao");
        System.out.println(JsonUtil.toJson(pictureDto));
    }

    @Test
    public void   getPageListConditions()throws Exception{
//        {name:“__userTag__",op:"in",value:"tag1,tag2"}
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
        l.add(conditions);

//        Conditions conditions1 = new Conditions();
//        conditions1.setName("type");
//        conditions1.setOp("like");
//        conditions1.setValue("%D%");
//        l.add(conditions1);
//
//        Conditions conditions2 = new Conditions();
//        conditions2.setName("name");
//        conditions2.setOp("is not null");
//        conditions2.setValue("11");
//        l.add(conditions2);
//
//
//        Conditions conditions3 = new Conditions();
//        conditions3.setName("status");
//        conditions3.setOp("not in");
//        String test1 = "1";
//        String test2 = "2";
//        conditions3.setValue("(" + test1 + "," + test2 + ")");
//        l.add(conditions3);

        commonQueryParam.setConditions(l);

        PageDto<PictureDto> pageDto= pictureService.getPageListConditions(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }
}
