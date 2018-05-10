package com.anyun.esb.component.host.service.docker.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/6/16
 */
public class DockerImagePullResultCallback implements ResultCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImagePullResultCallback.class);
    private String pullName;
    private DockerClient dockerClient;

    public DockerImagePullResultCallback(DockerClient dockerClient, String pullName) {
        this.dockerClient = dockerClient;
        this.pullName = pullName;
    }

    @Override
    public void onStart(Closeable closeable) {
        LOGGER.debug("Docker image [{}] pull start", pullName);
    }

    @Override
    public void onNext(Object o) {

    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        List<Image> images = dockerClient.listImagesCmd().exec();
        for (Image image : images) {
            dockerClient.removeImageCmd(image.getId()).exec();
        }
    }

    @Override
    public void onComplete() {
        LOGGER.debug("Docker image [{}] pull success", pullName);
    }

    @Override
    public void close() throws IOException {

    }
}
