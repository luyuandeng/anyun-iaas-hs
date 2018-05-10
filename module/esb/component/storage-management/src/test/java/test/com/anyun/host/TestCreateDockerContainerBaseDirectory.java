package test.com.anyun.host;

import com.anyun.esb.component.storage.client.HostSshClient;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by twitchgg on 16-8-1.
 */
public class TestCreateDockerContainerBaseDirectory extends BaseComponentTest {
    private String storage_base_dir = "/opt/storage/docker/overlayfs/192.168.1.154";
    private String storage_volume_base_dir = "/opt/docker/mount_point";
    private HostSshClient ssh;

    @Before
    public void setupSSHClient() throws Exception {
        String hostCert = "/home/twitchgg/Desktop/host_key";
        ssh = new HostSshClient(hostCert);
        ssh.setHost("192.168.1.154");
        ssh.connect();
    }

    /**
     * 通过块设备将Docker容器基本操作系统磁盘重定向到存储设备（gluster）
     *
     * @throws Exception
     */
    @Test
    public void testCreateDockerContainerStorageCreate() throws Exception {
        //create a test docker
        String testDockerName = "storage-test-container";
        System.out.println("1.Create test docker container [" + testDockerName + "]");
        String id = createTestDocker(testDockerName);
        System.out.println("Container id [" + id + "]");
        String containerWorkPath = findContainerRuntimeDirectory(id);
        String containerRoot = createContainerFileSystemRoot(id);
        System.out.println("2.Create container storage path [" + containerRoot + "]");
        System.out.println("3.copy base ext4 filesystem block to [" + containerRoot + "]");
        copyExistBlockToContainerBaseDir(id);
        System.out.println("4.Inspect container runtime data base directory  [" + containerWorkPath + "]");
        reorganizedContainerRuntimeDirectory(containerWorkPath);
        System.out.println("5.organize container runtime data base directory");
        System.out.println("6.Mount and sync data [" + containerWorkPath + "]...");
        mountAndRsyncData(containerRoot, containerWorkPath);
        System.out.println("Display container [" + id + "] work directory filesystem info ----");
        displayBlockDeviceInfo(containerWorkPath);
        System.out.println("Start container [" + id + "] and display container root filesystem info ----");
        startDockerAndDisplayContainerRootFileSystem(id);

        ///////////////////////////////////////////////////clear storage and container/////////////
        System.out.println("clear test env....................");
        String clearScript = "docker stop " + id;
        ssh.exec(clearScript);
        clearScript = "umount " + containerWorkPath;
        ssh.exec(clearScript);
        clearScript = "docker rm -f " + id + " && rm -rf " + (storage_base_dir + "/" + id);
        ssh.exec(clearScript);
        System.out.println("test finish ....................");
    }

    /**
     * 通过块设备在线添加Docker容器卷并且在线修改卷大小
     *
     * @throws Exception
     */
    @Test
    public void testCreateContainerDynamicVolumes() throws Exception {

//        String testDockerName = "storage-test-container";
//        System.out.println("1.Create test docker container [" + testDockerName + "]");
//        String id = createTestDocker(testDockerName);
//        System.out.println("Container id [" + id + "]");
        String id = "0d07aca51c8b81493131975653507a3c57ed0d0d0dc915380ed94f04dd50473f";
        String volumeMountPoint = storage_volume_base_dir + "/" + id + "/volume1";
        System.out.println("2.Magic container volume path");
        String dev = getVolumeLoopDevice(volumeMountPoint);
        System.out.println("Volume [" + volumeMountPoint + "] device [" + dev + "]");
        String devdec = getVolumeDevDec(dev);
        System.out.println("Device [" + dev + "] dec [" + devdec + "]");
        System.out.println("Binding volume block!");
        volumeBinding(id,dev,devdec,"/opt/test");
    }

    private String getVolumeDevDec(String dev) throws Exception {
        String cmd = "DEVDEC=$(printf '%d %d' $(stat --format '0x%t 0x%T' " + dev + ")) | echo $DEVDEC";
        return ssh.exec(cmd);
    }

    private String getVolumeLoopDevice(String volumeMountPoint) throws Exception {
        String cmd = "echo $(readlink --canonicalize " + volumeMountPoint + ")";
        String realPath = ssh.exec(cmd);
        System.out.println("HOSTPATH [" + realPath + "]");
        cmd = "echo $(df -P " + realPath + " | tail -n 1 | awk '{print $6}')";
        String fileSys = ssh.exec(cmd);
        cmd = "while read DEV MOUNT JUNK ; do [ $MOUNT = "
                + fileSys + " ] && break ;done </proc/mounts && echo $DEV";
        return ssh.exec(cmd);
    }

    private String volumeBinding(String id,String dev,String devdec,String dest) throws Exception{
        String cmd = "/usr/bin/docker-enter " + id + " sh -c "
                + "'[ -b " + dev + " ] || mknod --mode 0600 " + dev + " b " + devdec + "'";
        ssh.exec(cmd);
        cmd = "/usr/bin/docker-enter " + id + " mkdir -p " + dest;
        ssh.exec(cmd);
        cmd = "/usr/bin/docker-enter " + id + " mount " + dev + " " + dest;
        return ssh.exec(cmd);
    }


    private String startDockerAndDisplayContainerRootFileSystem(String id) throws Exception {
        String cmd = "docker start " + id;
        ssh.exec(cmd);
        cmd = "docker exec " + id + " df -h /";
        System.out.println(ssh.exec(cmd));
        return null;
    }

    private String displayBlockDeviceInfo(String workPath) throws Exception {
        String cmd = "df -h " + workPath;
        System.out.println(ssh.exec(cmd));
        return null;
    }

    private String mountAndRsyncData(String containerRoot, String directory) throws Exception {
        String bakDir = directory + ".bak";
        String block = containerRoot + "/base";
        String command = "mount -o loop " + block + " " + directory;
        ssh.exec(command);
        command = "rsync -r " + bakDir + "/* " + directory;
        return ssh.exec(command);
    }

    private String reorganizedContainerRuntimeDirectory(String directory) throws Exception {
        String bakDir = directory + ".bak";
        String commnad = "mv " + directory + " " + bakDir;
        ssh.exec(commnad);
        commnad = "mkdir -p " + directory;
        return ssh.exec(commnad);
    }

    private String findContainerRuntimeDirectory(String id) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("docker inspect --format='{{range $p, $p1 :=.GraphDriver.Data}} {{$p1}}{{end}}' ").append(id);
        sb.append(" | awk -F ' ' '{print $3}' | sed 's/upper/''/g'");
        sb.append(" | xargs readlink --canonicalize");
        return ssh.exec(sb.toString()).trim();
    }

    private String copyExistBlockToContainerBaseDir(String id) throws Exception {
        String existBlock = "/root/work/base";
        String dest = storage_base_dir + "/" + id;
        String command = "cp -v " + existBlock + " " + dest;
        return ssh.exec(command);
    }

    private String createContainerFileSystemRoot(String id) throws Exception {
        String containerRoot = "";
        String baseStorageDirPath = "/opt/storage/docker/overlayfs/192.168.1.154";
        StringBuilder sb = new StringBuilder();
        sb.append("mkdir -p ").append(baseStorageDirPath).append("/").append(id);
        ssh.exec(sb.toString());
        containerRoot = baseStorageDirPath + "/" + id;
        return containerRoot;
    }

    private String createTestDocker(String name) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("docker create -i -t --name ").append(name).append(" --hostname ").append(name);
        sb.append(" ").append("imagehub.anyuncloud.com/os/oem/ubuntu:14.04");
        return ssh.exec(sb.toString());
    }
}
