package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.ImageService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.l;

/**
 * Created by sxt on 16-7-18.
 */
public class TestImages extends BaseAnyunTest {
    private ImageService imageService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        imageService = factory.getImageService();
        ResourceClient.setUserToken("debug_token");
    }

    //1、查询 镜像 列表
    @Test
    public void getImageList() {
        String userUniqueId = "";


//        String   subMethod="QUERY_REGISTRY";
//        String   subParameters=null;

//        String   subMethod="QUERY_UNREGISTRY";
//        String   subParameters=null;

//        String   subMethod="QUERY_BY_CATEGORY";
//        String   subParameters="database";

        String subMethod = "QUERY_UNREGISTRY";
        String subParameters = "";

        List<DockerImageDto> l = imageService.queryImage(userUniqueId, subMethod, subParameters);
        for (DockerImageDto d : l) {
            System.out.println(d.asJson());
        }
    }

    //2、注册镜像
    @Test
    public void registryImage() {
        ImageRegistParam param = new ImageRegistParam();
        param.setId("4BF4AF4E85A54D732B50347812F152F0");
        param.setUserDefineName("123");
        param.setDescript("123");
        param.setIcon("123");
        param.setStatus(1);
        param.setUserUniqueId("ds");
        DockerImageDto status = imageService.registryDockerImage(param);
        System.out.print(status);
    }

    //3、修改镜像
    @Test
    public void updateImage() {
        ImageRegistParam param = new ImageRegistParam();
        param.setId("103F7CE642D91AC3F8DBEA6C323CE48D");
        param.setDescript("12345");
        param.setIcon("");
        param.setUserDefineName("gp1");
        param.setStatus(1);
        param.setUserUniqueId("q");
        DockerImageDto status = imageService.updateUserDockerImage(param);
        System.out.print(status);
    }

    //4、删除镜像
    @Test
    public void deleteImage() {
        List<String> ids = new ArrayList<String>();
        ids.add("E1AF7BC7371B7076BD65CC0C0BC6BFD8");
        String userUniqueId = "2";
        Status<String> status = imageService.deleteUserDockerImage(ids, userUniqueId);
        System.out.print(status.getStatus());
    }

    //5、查询镜像分类列表
    @Test
    public void getCategoryList() {
        String userUniqueId = "";
//         String subMethod = "QUERY_REGISTRY";
        String subMethod = "QUERY_UNREGISTRY";
        List<DockerImageCategoryDto> l = imageService.queryCategory(userUniqueId, subMethod);
        for (DockerImageCategoryDto dto : l) {
            System.out.println(JsonUtil.toJson(dto));
        }
    }

    //6、注册镜像分类
    @Test
    public void registryCategory() {
        ImageCategoryRegistParam param = new ImageCategoryRegistParam();
        param.setId("11E0EED8D3696C0A632F822DF385AB3C");
        param.setDescript("test");
        param.setShortName("gp");
        param.setUserUniqueId("");
        DockerImageCategoryDto dto = imageService.registDockerImageCategory(param);
        System.out.print(dto);
    }

    //7、修改镜像分类
    @Test
    public void updateCategory() {
        ImageCategoryRegistParam param = new ImageCategoryRegistParam();
        param.setId("11E0EED8D3696C0A632F822DF385AB3C");
        param.setDescript("SDK 测试5");
        param.setShortName("gp test2");
        param.setUserUniqueId("");
        DockerImageCategoryDto dto = imageService.updateUserDockerImageCategories(param);
        System.out.print(dto);
    }

    //8、删除镜像分类
    @Test
    public void deleteCategory() {
        List<String> id = new ArrayList<>();
        id.add("AAABF0D39951F3E6C3E8A7911DF524C2");
        String userUniqueId = "";
        Status<String> status = imageService.deleteUserDockerImageCategories(id, userUniqueId);
        System.out.print(status.getStatus());
    }

    //9、查询镜像分类信息列表
    @Test
    public void queryCategoryList(){
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
        l.add(conditions);

      /*  Conditions conditions1 = new Conditions();
        conditions1.setName("type");
        conditions1.setOp("like");
        conditions1.setValue("%D%");
        l.add(conditions1);

        Conditions conditions2 = new Conditions();
        conditions2.setName("name");
        conditions2.setOp("is not null");
        conditions2.setValue("11");
        l.add(conditions2);


        Conditions conditions3 = new Conditions();
        conditions3.setName("status");
        conditions3.setOp("not in");
        String test1 = "1";
        String test2 = "2";
        conditions3.setValue("(" + test1 + "," + test2 + ")");
        l.add(conditions3);*/

        //commonQueryParam.setConditions(l);

        PageDto<DockerImageCategoryDto> pageDto = imageService.queryCategoryList(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    //10、查询镜像信息列表
    @Test
    public void queryImageList(){
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
        commonQueryParam.setSortBy("name");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");

   List<Conditions> l = new ArrayList<>();

         Conditions conditions = new Conditions();
        conditions.setName("category");
        conditions.setOp("=");
        conditions.setValue("env+deve");
        l.add(conditions);

       /* Conditions conditions1 = new Conditions();
        conditions1.setName("type");
        conditions1.setOp("like");
        conditions1.setValue("%D%");
        l.add(conditions1);

        Conditions conditions2 = new Conditions();
        conditions2.setName("name");
        conditions2.setOp("is not null");
        conditions2.setValue("11");
        l.add(conditions2);


        Conditions conditions3 = new Conditions();
        conditions3.setName("status");
        conditions3.setOp("not in");
        String test1 = "1";
        String test2 = "2";
        conditions3.setValue("(" + test1 + "," + test2 + ")");
       // l.add(conditions3);*/

        commonQueryParam.setConditions(l);

        PageDto<DockerImageDto> pageDto = imageService.queryImageList(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }
}
