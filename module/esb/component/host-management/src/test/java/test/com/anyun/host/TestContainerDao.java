package test.com.anyun.host;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class TestContainerDao extends BaseComponentTest {
    private ContainerDao containerDao;

    @Before
    public void setup() throws Exception {
        containerDao=new ContainerDaoImpl();
    }

    @Test
    public void insertContainer(){
        Date date=new Date();
        ContainerDto containerDto=new ContainerDto();
        containerDto.setId("123");
        containerDto.setProjectId("1");
        containerDto.setType(1);
        containerDto.setImageId("1");
        containerDto.setImageName("1");
        containerDto.setStatus(1);
        containerDto.setCreateTime(date);
        containerDto.setLastModifyTime(date);
        containerDto.setHostName("fdfd");
        containerDto.setMemoryLimit(2);
        containerDto.setMemorySwapLimit(3);
        containerDto.setCpuFamily("fd");
        containerDto.setCpuCoreLimit(2);
        containerDto.setDiskSchemeId("1");
        containerDto.setCalculationSchemeId("fd");
        containerDto.setPrivileged(0);
        containerDto.setHostId("fdf");
        containerDto.setShortId("fdf");
        containerDto.setName("fdfd");
        containerDto.setPurpose("WEB");


        ContainerDto  c=  containerDao.insertContainer(containerDto);
        System.out.println(c.asJson());
    }

    @Test
    public void selectContainerListByDiskSchemeId(){
        List<ContainerDto> list=  containerDao.selectContainerListByDiskSchemeId("1");
        for(ContainerDto c:list)
        System.out.println(c.asJson());
    }

    @Test
    public void selectContainerListByCalculationSchemeId(){
        List<ContainerDto> l=containerDao.selectContainerListByCalculationSchemeId("1");
        for(ContainerDto c:l){
            System.out.println(c.asJson());
        }
    }

    @Test
    public void changeCalculationScheme(){
        String id="123";
        String calculationSchemeId="";
        containerDao.changeCalculationScheme(id,calculationSchemeId);
    }


    @Test
    public void changeDiskScheme(){
        String id="123";
        String diskSchemeId="";
        containerDao.changeDiskScheme(id,diskSchemeId);
    }
}
