package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.TagDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.param.TagCreateParam;
import com.anyun.cloud.param.TagUpdateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.TagService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/10.
 */
public class TestTag extends BaseAnyunTest {
    private TagService service;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        service = factory.getTagService();
        ResourceClient.setUserToken("debug_token");
    }

    //1.查询Tag
    @Test
    public void  getPageListConditions()throws Exception{
//        {name:“__userTag__",op:"in",value:"tag1,tag2"}
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
        commonQueryParam.setSortBy("id");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");

        List<Conditions> l = new ArrayList<>();


//        Conditions conditions1 = new Conditions();
//        conditions1.setName("resourceType");
//        conditions1.setOp("is not null");
//        conditions1.setValue("%D%");
//        l.add(conditions1);

        Conditions conditions2 = new Conditions();
        conditions2.setName("id");
        conditions2.setOp("is not null");
        conditions2.setValue("11");
        l.add(conditions2);


        Conditions conditions3 = new Conditions();
        conditions3.setName("id");
        conditions3.setOp("not in");
        String test1 = "1";
        String test2 = "2";
        conditions3.setValue("(" + test1 + "," + test2 + ")");
        l.add(conditions3);

        commonQueryParam.setConditions(l);


        PageDto<TagDto> pageDto= service.getListByconditions(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    //2.创建Tag
    @Test
    public  void TestCreate(){
        TagCreateParam param=new TagCreateParam();
        param.set__userTag__("image");
        param.setResourceId("123414");
        param.setResourceType("AssetsEE");
        TagDto dto = service.tagCreate(param);
        System.out.println(JsonUtil.toJson(dto));
    }

    //3.删除
    @Test
    public void deleteTag(){
        String id = "68eb8247014b400c8b29587935a446b9";
        String userUniqueId = "";
        Status<String> status = service.tagDelete(id,userUniqueId);
        System.out.println(status.getStatus());
    }

    //4.修改
    @Test
    public void updateTag(){
        TagUpdateParam param = new TagUpdateParam();
        param.setId("8197b1f93cce460a9b8e95395fa131c7");
        param.set__userTag__("qw");
        param.setResourceId("qw");
        param.setResourceType("qw");
        TagDto dto = service.tagUpdate(param);
        System.out.println(JsonUtil.toJson(dto));
    }

    //5.根据resourceId删除
    @Test
    public void deleteByResourceId(){
        String resourceId = "qqqq";
        String userUniqueId = "2";
        Status<String> status = service.tagDeleteByResourceId(resourceId,userUniqueId);
        System.out.println(status.getStatus());
    }
}
