/*
 *
 *      FileUtil.java
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

/**
 * 文件工具类
 *
 * @author TangLei <justinlei@gmail.com>
 * @date 2009-2-24
 */
@SuppressWarnings({"rawtypes"})
public class FileUtil {

    private FileUtil() {
    }

    public static void write(String fileName, String content, boolean append) {
        File file = new File(fileName);
        BufferedWriter writer;
        try {
            if (file.exists()) {
                writer = new BufferedWriter(new FileWriter(file, append));
            } else {
                writer = new BufferedWriter(new FileWriter(fileName));
            }
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
        }
    }

    /**
     * 获取随机的文件名称
     *
     * @param seed	随机种子
     * @return
     */
    public static String getRandomFileName(String seed) {
        byte[] ra = new byte[100];
        new Random().nextBytes(ra);
        StringBuilder build = new StringBuilder("");
        for (int i = 0; i < ra.length; i++) {
            build.append(Byte.toString(ra[i]));
        }
        String currentDate = Long.toString(new Date().getTime());
        seed = seed + currentDate + build.toString();
        return EncryptUtils.getMD5ofStr(seed).toLowerCase();
    }

    /**
     * 列出所有当前层的文件和目录
     *
     * @param dir	目录名称
     * @return fileList	列出的文件和目录
     */
    public static List<File> ls(String dir) {
        try {
            return Arrays.asList(new File(dir).listFiles());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 根据需要创建文件夹
     *
     * @param dirPath 文件夹路径
     * @param del	存在文件夹是否删除
     */
    public static void mkdir(String dirPath, boolean del) {
        File dir = new File(dirPath);
        try {
            if (dir.exists()) {
                if (del) {
                    rm(dir.getAbsolutePath(), del);
                } else {
                    return;
                }
            }
            dir.mkdirs();
        } catch (Exception e) {
        }
    }

    /**
     * 删除文件和目录
     *
     * @param path
     * @param self
     */
    public static void rm(String path, boolean self) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList == null || fileList.length == 0) {
                if (self) {
                    file.delete();
                }
            } else {
                for (File _file : fileList) {
                    rm(_file.getAbsolutePath(), true);
                }
            }
            if (self) {
                file.delete();
            }
        } else {
            if (self) {
                file.delete();
            }
        }
    }

    /**
     * 移动文件
     *
     * @param source 源文件
     * @param target 目标文件
     * @param cache	文件缓存大小
     * @throws Exception
     */
    public static void mv(String source, String target, int cache) throws Exception {
        if (source.trim().equals(target.trim())) {
            return;
        }
        byte[] cached = new byte[cache];
        FileInputStream fromFile = new FileInputStream(source);
        FileOutputStream toFile = new FileOutputStream(target);
        while (fromFile.read(cached) != -1) {
            toFile.write(cached);
        }
        toFile.flush();
        toFile.close();
        fromFile.close();
        new File(source).deleteOnExit();
    }

    /**
     * 把属性文件转换成Map
     *
     * @param propertiesFile
     * @return
     * @throws Exception
     */
    public static final Map<String, String> getPropertiesMap(String propertiesFile) throws Exception {
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(propertiesFile);
        properties.load(inputStream);
        Map<String, String> map = new HashMap<String, String>();
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet) {
            map.put((String) key, properties.getProperty((String) key));
        }
        return map;
    }

    public static final Map<String, String> getPropertiesMap(Class clazz, String fileName) throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = clazz.getResourceAsStream(fileName);
        if (inputStream == null) {
            inputStream = clazz.getClassLoader().getResourceAsStream(fileName);
        }
        properties.load(inputStream);
        Map<String, String> map = new HashMap<String, String>();
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet) {
            map.put((String) key, properties.getProperty((String) key));
        }
        return map;
    }

    /**
     * 把属性文件转换成Map
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static final Map<String, String> getPropertiesMap(InputStream inputStream) throws Exception {
        Properties properties = new Properties();
        properties.load(inputStream);
        Map<String, String> map = new HashMap<String, String>();
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet) {
            map.put((String) key, properties.getProperty((String) key));
        }
        return map;
    }

    /**
     * 把属性文件转换成Map
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static final Map<String, String> getPropertiesMap(File file) throws Exception {
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(file);
        properties.load(inputStream);
        Map<String, String> map = new HashMap<String, String>();
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet) {
            map.put((String) key, properties.getProperty((String) key));
        }
        inputStream.close();
        return map;
    }

    /**
     * 把文本文件转换成String
     *
     * @param fullPath
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String cat(String fullPath, String encoding) throws Exception {
        if (StringUtils.isEmpty(fullPath)) {
            return null;
        }
        StringBuilder builder;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fullPath), encoding))) {
            builder = new StringBuilder("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }
        return builder.toString();
    }

    public static String cat(File file, String encoding) {
        if (file == null) {
            return null;
        }
        String fullPath = file.getAbsolutePath();
        try {
            return cat(fullPath, encoding);
        } catch (Exception e) {
            return null;
        }
    }

    public static String cat(InputStream stream, String encoding) throws Exception {
        if (stream == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, encoding));
        StringBuilder builder = new StringBuilder("");
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }
        stream.close();
        reader.close();
        return builder.toString();
    }

    /**
     * 获取资源文件流
     *
     * @param clazz
     * @param name
     * @return
     */
    public static InputStream getResourceAsStream(Class clazz, String name) {
        if (clazz == null || StringUtils.isEmpty(name)) {
            return null;
        }
        try {
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream(name);
            if (inputStream == null) {
                inputStream = clazz.getResourceAsStream(name);
            }
            return inputStream;
        } catch (Exception e) {
            return null;
        }
    }

    public static URL getResource(Class clazz, String name) {
        if (clazz == null || StringUtils.isEmpty(name)) {
            return null;
        }
        try {
            URL inputStream = clazz.getClassLoader().getResource(name);
            if (inputStream == null) {
                inputStream = clazz.getResource(name);
            }
            return inputStream;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUnixPath(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        char[] chars = path.toCharArray();
        char[] unixChars = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\') {
                c = '/';
            }
            unixChars[i] = c;
        }
        return new String(unixChars);
    }

    public static boolean exist(String path, FileType fileType) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (fileType == null) {
            return true;
        }
        if (fileType.equals(FileType.DIR)) {
            if (file.isDirectory()) {
                return true;
            } else {
                return false;
            }
        } else if (fileType.equals(FileType.FILE)) {
            if (file.isFile()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static enum FileType {

        FILE, DIR
    }
}
