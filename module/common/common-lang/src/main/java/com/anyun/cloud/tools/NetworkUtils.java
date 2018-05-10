/*
 *
 *      NetworkUtils.java
 *      Copyright (C) <2015-?>  <twitchgg@yahoo.com>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.anyun.cloud.tools;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * @date 2014/11/14
 * @author TwitchGG
 * @version 1.0
 */
public class NetworkUtils {

    public static boolean isAddressAvailable(String address) {
        InetAddress _address;
        try {
            _address = InetAddress.getByName(address);//ping this IP 
            if (!(_address instanceof java.net.Inet4Address) && !(_address instanceof java.net.Inet6Address)) {
                System.err.println("非法的服務器名稱[" + address + "]");
                return false;
            }
        } catch (UnknownHostException ex) {
            System.err.println("非法的服務器名稱[" + address + "]");
            return false;
        }
        try {
            if (_address.isReachable(5000)) {
                return true;
            }
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                if (_address.isReachable(ni, 0, 5000)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean isReachableIP(String remote, int port) {
        if (!isAddressAvailable(remote)) {
            return false;
        }
        String retIP = null;
        Enumeration<NetworkInterface> netInterfaces;
        try {
            InetAddress remoteAddr = InetAddress.getByName(remote);
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> localAddrs = ni.getInetAddresses();
                while (localAddrs.hasMoreElements()) {
                    InetAddress localAddr = localAddrs.nextElement();
                    if (isReachable(localAddr, remoteAddr, port, 5000)) {
                        retIP = localAddr.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException | UnknownHostException e) {
//            System.out.println("Error occurred while listing all the local network addresses.");
            return false;
        }
        return retIP != null;
    }

    public static boolean isReachable(InetAddress localInetAddr, InetAddress remoteInetAddr, int port, int timeout) {
        Socket socket = null;
        try {
            socket = new Socket(); // 端口号设置为 0 表示在本地挑选一个可用端口进行连接 
            SocketAddress localSocketAddr = new InetSocketAddress(localInetAddr, 0);
            socket.bind(localSocketAddr);
            InetSocketAddress endpointSocketAddr = new InetSocketAddress(remoteInetAddr, port);
            socket.connect(endpointSocketAddr, timeout);
//            System.out.println("SUCCESS - connection established! Local: " + localInetAddr.getHostAddress() + " remote: " + remoteInetAddr.getHostAddress() + " port" + port);
            return true;
        } catch (IOException e) {
//            System.out.println("FAILRE - CAN not connect! Local: " + localInetAddr.getHostAddress() + " remote: " + remoteInetAddr.getHostAddress() + " port" + port);
            return false;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error occurred while closing socket..");
                }
            }
        }
    }

    public static boolean isMask(String mask) {
        Pattern pattern = Pattern.compile("(^((\\d|[01]?\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[01]?\\d\\d|2[0-4]\\d|25[0-5])$)|^(\\d|[1-2]\\d|3[0-2])$");
        return pattern.matcher(mask).matches();
    }

    public static String mask2ipMask(Integer inetMask) {
        String ipMask = "";
        int part = inetMask / 8;
        int remainder = inetMask % 8;
        int sum = 0;
        for (int i = 8; i > 8 - remainder; i--) {
            sum = sum + (int) Math.pow(2, i - 1);
        }
        if (part == 0) {
            ipMask = sum + ".0.0.0";
        } else if (part == 1) {
            ipMask = "255." + sum + ".0.0";
        } else if (part == 2) {
            ipMask = "255.255." + sum + ".0";
        } else if (part == 3) {
            ipMask = "255.255.255." + sum;
        } else if (part == 4) {
            ipMask = "255.255.255.255";
        }
        return ipMask;
    }
}
