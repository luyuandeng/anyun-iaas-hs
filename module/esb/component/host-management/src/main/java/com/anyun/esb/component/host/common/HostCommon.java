package com.anyun.esb.component.host.common;

import com.anyun.cloud.dto.HostExtInfoDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.client.HostSshClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class HostCommon {
    public static List<byte[]> getRangeIps(List<byte[]> list,byte[] addrBytes,byte[] endBytes) throws Exception {
        byte[] _b = new byte[addrBytes.length];
        System.arraycopy(addrBytes, 0, _b, 0, addrBytes.length);
        if(_b[3] == -1)
            _b[3] = -2;
        if(list == null) {
            list = new ArrayList<byte[]>();
            list.add(_b);
        }
        if(list.size() == 10000)
            return list;
        if(addrBytes[2] == -1 && addrBytes[3] == -2)
            return list;
        if(Arrays.equals(addrBytes, endBytes)) {
            return list;
        }
        int lastpart = addrBytes[addrBytes.length - 1];
        if(lastpart != -2) {
            addrBytes[addrBytes.length - 1] = (byte)(addrBytes[3] + 1);
            _b = new byte[addrBytes.length];
            System.arraycopy(addrBytes, 0, _b, 0, addrBytes.length);
            list.add(_b);
        } else if(lastpart == -2) {
            addrBytes[addrBytes.length - 1] = 1;
            list.add(plus(addrBytes, addrBytes.length - 2));
        }
        return getRangeIps(list,addrBytes, endBytes);
    }

    public static byte[] plus(byte[] addrBytes,int index) throws Exception {
        if(index == 0)
            return addrBytes;
        int pluspart = addrBytes[index];
        if(pluspart != -2) {
            addrBytes[index] = (byte)(pluspart + 1);
            for (int i = index + 1; i < addrBytes.length - 1; i++) {
                addrBytes[i] = 0;
            }
            byte[] _b = new byte[addrBytes.length];
            System.arraycopy(addrBytes, 0, _b, 0, addrBytes.length);
            return _b;
        } else {
            byte[] _b = new byte[addrBytes.length];
            System.arraycopy(addrBytes, 0, _b, 0, addrBytes.length);
            return plus(_b, index -1);
        }
    }
}
