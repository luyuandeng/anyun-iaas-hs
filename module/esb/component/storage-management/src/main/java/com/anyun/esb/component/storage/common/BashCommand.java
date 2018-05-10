package com.anyun.esb.component.storage.common;

/**
 * Created by twitchgg on 16-8-1.
 */
public class BashCommand {

    /**
     * 根据类型查询宿主机文件系统挂载信息
     * command sample :
     * echo `mount | grep 'fuse.gluster'` | awk -F ' ' '{print $1,$3,$5,$6}'
     *
     * @param type
     * @return
     */
    public static String queryMountFS(String type) {
        String command = "echo `mount | grep '" + type + "'` | awk -F ' ' '{print $1,$3,$5,$6}'";
        return command;
    }

    /**
     * 根据文件类型查询宿主机文件系统挂载信息（显示列选择）
     * command sample :
     * echo `mount | grep 'fuse.gluster'` | awk -F ' ' '{print [columes]}'
     *
     * @param type    ps: $1,$3,$5,$6
     * @param columns
     * @return
     */
    public static String queryMountFS(String type, String columns) {
        String command = "echo `mount | grep '" + type + "'` | awk -F ' ' '{print " + columns + "}'";
        return command;
    }

    /**
     * 挂载远程文件系统
     * command sample:
     * mount -t glusterfs 192.168.1.151:/test-volume1 /opt/storage
     * mount -t nfs 192.168.1.151:/nfs/volume1 /opt/storage/nfs
     *
     * @param type
     * @param addr
     * @param dest
     * @return
     */
    public static String mountFS(String type, String addr, String dest) {
        StringBuilder sb = new StringBuilder();
        sb.append("mount -t ").append(type);
        sb.append(" ").append(addr);
        sb.append(" ").append(dest);
        return sb.toString();
    }

    /**
     * 根据路径获取文件系统信息
     * command sample:
     * readlink --canonicalize /opt/storage | xargs df -h | sed '1d'
     * $1:Filesystem,$2:Size,$3:Used,$4:Avail,$5:Use%,$6:Mounted on
     *
     * @param realPath
     * @return
     */
    public static String getMountedInfo(String realPath) {
        String command = "readlink --canonicalize " + realPath + " | xargs df -h | sed '1d'";
        return command;
    }

    /**
     * 获取docker root真实路径
     * command sample:
     * docker info | grep 'Docker Root Dir' | awk -F ':' '{print $2}' | xargs readlink --canonicalize
     *
     * @return path ps: /var/lib/docker
     */
    public static String getDockerRootDirRealPath() {
        StringBuilder sb = new StringBuilder();
        sb.append("docker info | grep 'Docker Root Dir' ");
        sb.append("| awk -F ':' '{print $2}' ");
        sb.append("| xargs readlink --canonicalize");
        return sb.toString();
    }
}
