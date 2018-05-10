package test.com.anyun;

import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.ImageCategoryRegistParam;
import com.anyun.cloud.param.ImageRegistParam;
import com.anyun.cloud.param.UserIdsParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.JbiCommon;
import com.anyun.esb.component.registry.common.Env;
import com.anyun.esb.component.registry.common.HostSshClient;
import com.anyun.esb.component.registry.service.core.RegistryService;
import com.anyun.esb.component.registry.service.core.impl.RegistryServiceImpl;
import com.anyun.esb.component.registry.service.dao.DockerImageCategoryDao;
import com.anyun.esb.component.registry.service.dao.DockerImageDao;
import com.anyun.esb.component.registry.service.dao.impl.DockerImageCategoryDaoImpl;
import com.anyun.esb.component.registry.service.dao.impl.DockerImageDaoImpl;
import com.anyun.esb.component.registry.service.publish.*;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/4/16
 */
public class DockerImageRegistryTest extends BaseComponentTest {
    private static final String REGISTRY_HOSTNAME = "imagehub.anyuncloud.com";
    private RegistryService registryService;
    private DockerImageDao  dockerImageDao;

    @Before
    public void setUp() throws Exception {
        Jedis jedis = Env.env(Jedis.class);
        String registryHostKey = jedis.get("com.anyun.certs.path") + "/registry_rsa";
        HostSshClient hostSshClient = new HostSshClient(registryHostKey);
        hostSshClient.setHost(REGISTRY_HOSTNAME);
        hostSshClient.connect();
        Env.set("REGISTRY.CLIENT", hostSshClient);
        registryService = new RegistryServiceImpl();
    }

    @Test
    public  void    testSelectImageByConditions(){
        dockerImageDao  =  new DockerImageDaoImpl();
        PageDto<DockerImageDto> dtos = dockerImageDao.selectImageByConditions(null,null,1,4);
        System.out.println("PageNo : " + dtos.getPageNumber());
        System.out.println("PageSize : " + dtos.getPageSize());
        System.out.println("Total : " + dtos.getTotal());
        System.out.println("Content : \n" + JbiCommon.toJson(dtos.getData()) + "\n");
    }

    @Test
    public  void    testSelectById(){
        dockerImageDao  =  new DockerImageDaoImpl();
        DockerImageDto    dto =dockerImageDao.selectById("2");
        System.out.print(JsonUtil.toJson(dto));
    }

    @Test
    public void  testInsertImageDocker(){
        dockerImageDao  =  new DockerImageDaoImpl();
        DockerImageDto  dto =  new DockerImageDto();
        dto.setCategory("2");
        dto.setDescript("测试");
        dto.setIcon("测试");
        dto.setId("5");
        dto.setName("测试");
        dto.setStatus(1);
        dto.setTag("测试");
        dockerImageDao.insertUnregistDockerImages(dto);
    }

    @Test
    public void  testUpdateStatus(){
        dockerImageDao  =  new DockerImageDaoImpl();
        dockerImageDao.updateStatus(new DockerImageDto());
    }

    @Test
    public void  testBatchUpdateStatus(){
        dockerImageDao  =  new DockerImageDaoImpl();
        List<String>  l  =   new ArrayList<String>();
        l.add("1");
        l.add("2");
        l.add("3");
        l.add("4");
        dockerImageDao.batchUpdateStatus(l,0);
    }

    @Test
    public void testUpdate(){
        dockerImageDao = new DockerImageDaoImpl();
        DockerImageDto  dto = new DockerImageDto();
        dto.setId("76a034f7483042f59b0bba3348ad857b");
        dto.setCategory("234");
        dto.setDescript("234");
        dto.setIcon("234");
        dto.setName("243");
        dto.setStatus(0);
        dto.setTag("123");
        dockerImageDao.update(dto);
    }

    @Test
    public void testSelectByFullName(){
        dockerImageDao = new DockerImageDaoImpl();
        DockerImageDto  dto =  new DockerImageDto();
        List<DockerImageDto>  l=  dockerImageDao.selectByFullName("2","2");
        System.out.print(JsonUtil.toJson(l));

    }

    @Test
    public  void  testDeleteDocker(){
        UserIdsParam    param  = new UserIdsParam();
        List<String>    l   = new ArrayList<String>();
        l.add("1");
        l.add("2");
        param.setIds(l);
        param.setUser("i");
        exchange.getIn().setBody(param.asJson());
        DockerImageDeleteByUserService delete=new DockerImageDeleteByUserService();
        delete.process(endpoint,exchange);
    }

    @Test
    public  void  testUpdateDockerImageService(){
        DockerImageUpdateByUserService dockerImageUpdateByUserService = new DockerImageUpdateByUserService();
        ImageRegistParam updateParam  = new ImageRegistParam();
        updateParam.setId("76a034f7483042f59b0bba3348ad857b");
        updateParam.setDescript("2");
        updateParam.setIcon("2");
        updateParam.setUserDefineName("2");
        exchange.getIn().setBody(updateParam.asJson());
        dockerImageUpdateByUserService.process(endpoint,exchange);
    }



    @Test
    public void  testRegistryCategoryService(){
        ImageCategoryRegistParam registParam  = new ImageCategoryRegistParam();
        DockerImageCategoryRegistryService dockerImageCategoryRegistryService = new DockerImageCategoryRegistryService();
        registParam.setId("8");
        registParam.setShortName("1");
        registParam.setDescript("");
        registParam.setShortName(null);
        exchange.getIn().setBody(registParam.asJson());
        dockerImageCategoryRegistryService.process(endpoint,exchange);
    }

    @Test
    public  void   testRegistryCategoryServiceDao(){
        DockerImageCategoryDao  dao   =  new DockerImageCategoryDaoImpl();
        DockerImageCategoryDto  dto   = new DockerImageCategoryDto();
        dto.setId("4");
        dto.setName("4");
        dto.setDescript("4");
        dto.setShortName("4");
        dao.insertUnregistDockerImageCategories(dto);
    }


    @Test
    public  void  TestDockerImageRegistryService(){
        DockerImageRegistryService   service = new DockerImageRegistryService();
        ImageRegistParam  param = new ImageRegistParam();
        param.setId("7ed42aeba3f34dd3a8ddef2f3014772c");
        param.setUserDefineName("123");
        param.setDescript("123");
        param.setIcon("123");
        exchange.getIn().setBody(param.asJson());
        service.process(endpoint,exchange);
    }

    @Test
    public  void testDockerImageQuery(){
        DockerImageQueryService  dockerImageQueryService=new DockerImageQueryService();
        Map<String,Object>   param=new HashMap<String,Object>();
        param.put("subMethod","queryUnregistryImages");
        exchange.getIn().setHeaders(param);
        List<DockerImageDto>   l= (List<DockerImageDto>)dockerImageQueryService.process(endpoint,exchange);

    }
}
