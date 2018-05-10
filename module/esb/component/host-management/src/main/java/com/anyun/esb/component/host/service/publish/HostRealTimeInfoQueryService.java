package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.dto.HostRealTimeDto;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.DockerHostService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.esb.component.host.service.docker.impl.DockerHostServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-11-22.
 */
public class HostRealTimeInfoQueryService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(HostRealTimeInfoQueryService.class);
    private HostBaseInfoDao hostBaseInfoDao;
    private ContainerService containerService;
    private DockerHostService dockerHostService;

    public HostRealTimeInfoQueryService() {
        hostBaseInfoDao = new HostBaseInfoDaoImpl();
        containerService = new ContainerServiceImpl();
        dockerHostService = new DockerHostServiceImpl();
    }

    @Override
    public String getName() {
        return "hostRealTimeInfo_query";
    }

    @Override
    public String getDescription() {
        return "HostRealTimeInfoQueryService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        List<HostBaseInfoDto> list = hostBaseInfoDao.selectAllHostInfo();
        if (list == null)
            return new ArrayList<HostRealTimeDto>();
        LOGGER.debug("宿主机:[{}]", JsonUtil.toJson(list));
        try {
            //去除zookpeer 中没有的
            List<String> hostIp = dockerHostService.findAllActiveHosts();
            LOGGER.debug("Find action host:[{}]", hostIp);
            if (hostIp.isEmpty())
                return new ArrayList<HostRealTimeDto>();

            List<String> l = new ArrayList<>();
            for (String h : hostIp) {
                l.add(h.split(":")[0]);
            }

            LOGGER.debug("hostIp:{}", JsonUtil.toJson(hostIp));

            for (int i = list.size() - 1; i >= 0; i--) {
                if (!l.contains(list.get(i).getId()))
                    list.remove(i);
            }

            LOGGER.debug("list:{}", JsonUtil.toJson(list));
        } catch (Exception ex) {
            LOGGER.debug("find action  host fail :[{}]", ex);
        }

        List<HostRealTimeDto> hostRealTimeDtos = new ArrayList<>();
        for (HostBaseInfoDto h : list) {
            HostRealTimeDto hostRealTimeDto = new HostRealTimeDto();
            //查询某个宿主机器上所有容器
            try {
                List<ContainerDto> l = containerService.queryAllContainerByHost(h.getId());
                if (l == null || l.isEmpty()) {
                    hostRealTimeDto.setContainerTotal(0);
                    hostRealTimeDto.setMemoryUsed(0);
                    hostRealTimeDto.setMemoryUnused(h.getHostExtInfoDto().getMemoryTotal() / (1024 * 1024));
                    hostRealTimeDto.setPercentageUnUsed("100%");
                    hostRealTimeDto.setPercentageUsed("0%");
                } else {
                    LOGGER.debug("容器数量:[{}]", l.size());
                    long m = 0;
                    for (ContainerDto c : l) {
                        m += c.getMemoryLimit();
                    }
                    hostRealTimeDto.setContainerTotal(l.size());
                    long used = m;
                    long unUsed = h.getHostExtInfoDto().getMemoryTotal() / (1024 * 1024) - used;
                    hostRealTimeDto.setMemoryUsed(m);
                    hostRealTimeDto.setMemoryUnused(unUsed);
                    NumberFormat formatter = new DecimalFormat("0.00");
                    Double x = new Double((double) used / (double) unUsed);
                    hostRealTimeDto.setPercentageUsed(formatter.format(x * 100) + "%");
                    hostRealTimeDto.setPercentageUnUsed(formatter.format((1 - x) * 100) + "%");
                }
                hostRealTimeDto.setHost(h.getId());
                hostRealTimeDto.setName(h.getName());
                hostRealTimeDto.setCluster(h.getCluster());
                hostRealTimeDto.setIp(h.getHostManagementIpInfoDto().getIpAddress());
                hostRealTimeDto.setCpuCores(h.getHostExtInfoDto().getCpuCores());
                hostRealTimeDto.setCpuModel(h.getHostExtInfoDto().getCpuModel());
                hostRealTimeDto.setMemoryTotal(h.getHostExtInfoDto().getMemoryTotal() / (1024 * 1024));
                hostRealTimeDto.setStatus(h.getStatus());

                //TODO 获取cpu 使用率  ps -aux | awk '{sum+=$3} END{print sum}'
                String cpuUsed = "";//   cpu   已使用百分比
                String cpuUnUsed = "";// cpu   未使用百分比
                HostSshClient hostSshClient = Env.getSshClient(h.getId() + ":" + h.getHostManagementIpInfoDto().getIpAddress());
                String cmd = "ps -aux | awk '{sum+=$3} END{print sum}'";
                String response = hostSshClient.exec(cmd);
                LOGGER.debug("response:[{}]", response);
                NumberFormat form = new DecimalFormat("0.00");
                cpuUsed = form.format(Double.parseDouble(response.trim())) + "%";
                cpuUnUsed = form.format(100 - Double.parseDouble(response.trim())) + "%";
                hostRealTimeDto.setCpuUsed(cpuUsed);
                hostRealTimeDto.setCpuUnUsed(cpuUnUsed);
                hostRealTimeDtos.add(hostRealTimeDto);
            } catch (Exception e) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("宿主机实时信息查询失败[" + e.getMessage() + "]");
                exception.setUserMessage(e.getMessage());
                exception.setUserTitle("宿主机实时信息查询失败");
                throw exception;
            }
        }
        return hostRealTimeDtos;
    }
}
