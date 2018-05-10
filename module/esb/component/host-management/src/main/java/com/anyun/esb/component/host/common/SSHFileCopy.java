package com.anyun.esb.component.host.common;

import com.jcraft.jsch.*;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 17-3-30.
 */
public class SSHFileCopy {
    private final static Logger LOGGER = LoggerFactory.getLogger(SSHFileCopy.class);
    private static Session sourceSession; //源 session
    private static Session targetSession; //目标 session

    public void setSourceSession(Session sourceSession) {
        this.sourceSession = sourceSession;
    }

    public void setTargetSession(Session targetSession) {
        this.targetSession = targetSession;
    }

    public SSHFileCopy(Session sourceSession, Session targetSession) {
        setSourceSession(sourceSession);
        setTargetSession(targetSession);
    }

    /**
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    public void copy(String sourcePath, String targetPath) throws JSchException, IOException, SftpException {
        LOGGER.debug("sourcePath:[{}]", sourcePath);
        LOGGER.debug("targetPath:[{}]", targetPath);
        String[] s=sourcePath.split("/");
        String[] zip=s[s.length-1].split("\\.");
        String filename=zip[0]+"."+zip[1];
        File localTemporaryFile = File.createTempFile(zip[0], "."+zip[1]);
        LOGGER.debug("临时文件创建成功  path:[{}]", localTemporaryFile.getPath());

        //下载
        Channel channel1 = sourceSession.openChannel("sftp");
        ChannelSftp channelSftp1 = (ChannelSftp) channel1;
        channel1.connect();
        channelSftp1.get(sourcePath, localTemporaryFile.getParent());
        channel1.disconnect();
        LOGGER.debug("download  success-----------------------------------------");

        //上传
        Channel channel2 = targetSession.openChannel("sftp");
        ChannelSftp channelSftp2 = (ChannelSftp) channel2;
        channel2.connect();
        channelSftp2.put(localTemporaryFile.getParent()+"/"+filename, targetPath);
        channel2.disconnect();

        LOGGER.debug("upload     success-----------------------------------------");

        File file=new File(localTemporaryFile.getParent()+"/"+filename);

        if (localTemporaryFile.exists()) {
            localTemporaryFile.delete();
        }

        if (file.exists()){
            file.delete();
        }

        LOGGER.debug("临时文件删除成功");
    }
}
