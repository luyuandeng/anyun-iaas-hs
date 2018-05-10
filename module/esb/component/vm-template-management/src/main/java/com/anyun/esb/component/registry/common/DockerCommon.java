package com.anyun.esb.component.registry.common;

import com.anyun.cloud.dto.DockerImageCategoryDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.tools.EncryptUtils;
import com.anyun.cloud.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class DockerCommon {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerCommon.class);
    private static final String REGISTRY_PATH = "/data/docker/registry/v2/repositories";

    public static List<DockerImageCategoryDto> getImageCategories() throws Exception {
        List<DockerImageCategoryDto> dockerImageCategoryDtos = new ArrayList<>();
        List<DockerImageDto> dtos = getImageDtos();
        List<String> _nanmes = new ArrayList<>();
        for (DockerImageDto dto : dtos) {
            if (_nanmes.contains(dto.getCategory()))
                continue;
            DockerImageCategoryDto dockerImageCategoryDto = new DockerImageCategoryDto();
            dockerImageCategoryDto.setId(EncryptUtils.getMD5ofStr(dto.getCategory()));
            dockerImageCategoryDto.setName(dto.getCategory());
            dockerImageCategoryDtos.add(dockerImageCategoryDto);
            _nanmes.add(dto.getCategory());
        }
        return dockerImageCategoryDtos;
    }

    public static List<DockerImageDto> getImageDtos() throws Exception {
        List<DockerImageDto> dtos = new ArrayList<>();
        Jedis jedis = Env.env(Jedis.class);
        HostSshClient hostSshClient = Env.env(HostSshClient.class, "REGISTRY.CLIENT");
        LOGGER.debug("SSH Client [{}]", hostSshClient);
        String path = jedis.get("com.anyun.docker.registry.path.storage");
        path = path + REGISTRY_PATH;
        StringBuilder sb = new StringBuilder();
        sb.append("find " + path);
        sb.append(" -type d");
        sb.append(" | grep '/tags/' ");
        sb.append(" | grep -v 'index' ");
        sb.append(" | grep -v '/current' ");
        sb.append(" | sed 's/\\.\\//''/g' ");
        sb.append(" | sed 's/\\/_manifests\\/tags\\//':'/g'");
        String command = sb.toString();
        LOGGER.debug("Image query command [{}]", sb.toString());
        LOGGER.debug("SSH Client [{}]", hostSshClient);
        String result = hostSshClient.exec(command);
        LOGGER.debug("Result [{}]", result);
        List<String> images = StringUtils.readStringLines(result);
        for (String imagePath : images) {
            String image = imagePath.replaceAll(path + "/", "").trim();
            String[] content = image.split(":");
            String hash = EncryptUtils.getMD5ofStr(image);
            String[] imageNameInfo = StringUtils.getSplitValues(content[0], "/");
            String imageName = imageNameInfo[imageNameInfo.length - 1];
            String category = content[0].substring(0, content[0].length() - imageName.length() - 1);
            DockerImageDto dockerImageDto = new DockerImageDto();
            dockerImageDto.setId(hash);
            dockerImageDto.setTag(content[1]);
            dockerImageDto.setName(imageName);
            dockerImageDto.setCategory(category);
            dtos.add(dockerImageDto);
        }
        return dtos;
    }

    public static DockerImageDto getImage(String id) throws Exception {
        List<DockerImageDto> dtos = getImageDtos();
        for (DockerImageDto dto : dtos) {
            if (dto.getId().equals(id))
                return dto;
        }
        return null;
    }
}
