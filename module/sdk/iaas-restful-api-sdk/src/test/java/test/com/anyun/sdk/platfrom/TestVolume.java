package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.dto.ContainerVolumeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.VolumeService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gp on 17-5-5.
 */
public class TestVolume extends BaseAnyunTest {
    private VolumeService volumeService;


    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        volumeService = factory.getVolumeService();
        ResourceClient.setUserToken("debug_token");
    }


    //1、查询卷详情
    @Test
    public void getVolumeDetails() {
        String id = "e2a02bf03f1b44f6ade7f39016e03a70";
        String userUniqueId = "";
        VolumeDto dto = volumeService.volumeQueryById(id, userUniqueId);
        System.out.print(dto);
    }

    //2、查询卷列表
    @Test
    public void getVolumeList() {
//         QUERY_BY_PROJECT
//         QUERY_BY_CONTAINER
//         QUERY_UNMOUNTED_BY_PROJECT_CONTAINER
//         QUERY_BY_APPID
        String userUniqueId = "";
        String subMethod = "QUERY_BY_CONTAINER";
        String subParameters = "2dd407263cdf3269b391329661d73b26c1f67a9fe26c7319b262c9897bfa542b";
        List<VolumeDto> l = volumeService.volumeQuery(userUniqueId, subMethod, subParameters);
    }

    //3、删除卷
    @Test
    public void deleteVolume() {
        String id = "42b54a809f6b4b3ebaa2d493a2391769";
        String userUniqueId = "";
        volumeService.volumeDeleteById(id, userUniqueId);
    }

    //4、创建卷
    @Test
    public void createVolume() {
        VolumeCreateParam param = new VolumeCreateParam();
        param.setDescr("创建卷测试1");
        param.setName("5");
        param.setProject("d888bbd4573a43179aa156f41ed25520");
        param.setSpace(1);
        param.setUserUniqueId("2");
        param.setStorageId("08650108bf9a46229255e754109025fe");
        VolumeDto dto = volumeService.volumeCreate(param);
        System.out.print(JsonUtil.toJson(dto));
    }

    //5、修改卷
    @Test
    public void updateVolume() {
        VolumeUpdateParam param = new VolumeUpdateParam();
        param.setId("42b54a809f6b4b3ebaa2d493a2391769");
        param.setName("测试");
        param.setDescr("测试");
        param.setSpace(3);
        param.setUserUniqueId("2");
        VolumeDto dto = volumeService.volumeUpdate(param);
        System.out.print(JsonUtil.toJson(dto));
    }

    //6、容器挂载卷
    @Test
    public void containerMountVolume() {
        ContainerMountVolumeParam param = new ContainerMountVolumeParam();
        param.setContainer("2dd407263cdf3269b391329661d73b26c1f67a9fe26c7319b262c9897bfa542b");
        param.setVolume("282cac0a682a44c88a34c51ca2069268");
        param.setMountPath("/var");
        param.setUserUniqueId("");
        volumeService.containerMountVolume(param);
    }

    //7、容器卸载卷
    @Test
    public void containerUninstallVolume() {
        ContainerUninstallVolumeParam param = new ContainerUninstallVolumeParam();
        param.setContainer("2dd407263cdf3269b391329661d73b26c1f67a9fe26c7319b262c9897bfa542b");
        param.setVolume("42b54a809f6b4b3ebaa2d493a2391769");
        param.setUserUniqueId("");
        volumeService.containerUninstallVolume(param);
    }


    //8、查询卷由存储
    @Test
    public void queryVolumeByStorage() {
        String id = "08650108bf9a46229255e754109025fe";
        String userUniqueId = "2";
        List<VolumeDto> list = volumeService.queryVolumeByStroage(id, userUniqueId);
        System.out.print(JsonUtil.toJson(list));
    }

    //9、查询卷信息列表
    @Test
    public void queryVolumeList() {
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

        Conditions conditions1 = new Conditions();
        conditions1.setName("storageId");
        conditions1.setOp("=");
        conditions1.setValue("08650108bf9a46229255e754109025fe");
        l.add(conditions1);

/*

        Conditions conditions1 = new Conditions();
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
        l.add(conditions3);
*/

        commonQueryParam.setConditions(l);

        PageDto<VolumeDto> pageDto = volumeService.queryVolumeList(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    @Test
    public void getContainerMountVolumeInfo() {
        String container = "2";
        String volume = "3";
        String userUniqueId = "";
        List<ContainerVolumeDto> l = volumeService.getContainerMountVolumeInfo(container, volume, userUniqueId);
        for (ContainerVolumeDto c : l) {
            System.out.println(c.asJson());
        }
    }
}




