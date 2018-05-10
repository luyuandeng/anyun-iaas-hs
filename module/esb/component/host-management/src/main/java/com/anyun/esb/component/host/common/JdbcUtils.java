package com.anyun.esb.component.host.common;

import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.tools.StringUtils;
import com.mysql.jdbc.PreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 17-4-27.-
 *
 */
public class JdbcUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(JdbcUtils.class);
    //数据库连接参数
    private final static String url = "jdbc:mysql://cluster.mysql.anyuncloud.com/anyuncloud?useUnicode=true&characterEncoding=utf8&autoReconnect=true";
    private final static String username = "root";
    private final static String password = "111111";
    private final static String driverClass = "com.mysql.jdbc.Driver";
    private static Connection conn = null;


    //创建数据库连接
    public static Connection getConn() {
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            LOGGER.debug(e.getMessage());
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        }
        LOGGER.debug("Create a database connection success !");
        return conn;
    }


    /*
     * 将rs结果转换成对象列表
     * @param rs jdbc结果集
     * @param clazz 对象的映射类
     * return 封装了对象的结果列表
     */
    public static List populate(ResultSet rs, Class clazz) throws SQLException, InstantiationException, IllegalAccessException {
        //结果集的元素对象
        ResultSetMetaData rsmd = rs.getMetaData();
        //获取结果集的元素个数
        int colCount = rsmd.getColumnCount();
        //返回结果的列表集合
        List list = new ArrayList();
        //业务对象的属性数组
        Field[] fields = clazz.getDeclaredFields();
        while (rs.next()) {//对每一条记录进行操作
            Object obj = clazz.newInstance();//构造业务对象实体
            //将每一个字段取出进行赋值
            for (int i = 1; i <= colCount; i++) {
                Object value = rs.getObject(i);
                //寻找该列对应的对象属性
                for (int j = 0; j < fields.length; j++) {
                    Field f = fields[j];
                    //如果匹配进行赋值
                    if (f.getName().equalsIgnoreCase(rsmd.getColumnName(i))) {
                        boolean flag = f.isAccessible();
                        f.setAccessible(true);
                        f.set(obj, value);
                        f.setAccessible(flag);
                    }
                }
            }
            list.add(obj);
        }
        return list;
    }

    public static List getList(Class clazz, String sql) throws InstantiationException, IllegalAccessException, IllegalArgumentException, ClassNotFoundException {
        if (conn == null)
            conn = getConn();
        ResultSet rs = null;
        PreparedStatement psmt = null;
        List list = null;
        try {
            psmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            list = JdbcUtils.populate(rs, clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.debug(e.getMessage());
                }
                rs = null;
            }
            if (psmt != null) {
                try {
                    psmt.close();
                } catch (SQLException e) {
                    LOGGER.debug(e.getMessage());
                }
                psmt = null;
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.debug(e.getMessage());
                }
                conn = null;
            }
        }
        return list;
    }

    //list to String
    public static String ListToString(List<Conditions> list) throws Exception {
        if (list == null || list.isEmpty() || list.size() == 0)
            return "empty";
        StringBuffer stringBuffer = new StringBuffer();
        int i = 1;//计数
        for (Conditions c : list) {
            if (c == null)
                continue;
            if (StringUtils.isEmpty(c.getName()))
                continue;
            if (StringUtils.isEmpty(c.getOp()))
                continue;

            stringBuffer.append(" ");
            String name = c.getName().trim();
            String op = c.getOp().trim();
            String value = c.getValue().trim();
            if(value.contains("+")){
                LOGGER.debug("old-----value:[{}]",value);
                value=value.replace("+","/");
                LOGGER.debug("new-----value:[{}]",value);
            }
            LOGGER.debug("condition:[{}]------name:[{}] ---- op:[{}] ---- value:[{}]", i++, name, op, value);
            //操作符号判断
            switch (op) {
                case "=":
                    stringBuffer.append(name);
                    stringBuffer.append(" ");
                    stringBuffer.append(op);
                    stringBuffer.append(" ");
                    stringBuffer.append(getNewValue(value));
                    break;

                case "!=":
                    stringBuffer.append(name);
                    stringBuffer.append(" ");
                    stringBuffer.append(op);
                    stringBuffer.append(" ");
                    stringBuffer.append(getNewValue(value));
                    break;

                case ">":
                    stringBuffer.append(name);
                    stringBuffer.append(" ");
                    stringBuffer.append(op);
                    stringBuffer.append(" ");
                    stringBuffer.append(getNewValue(value));
                    break;

                case "<":
                    stringBuffer.append(name);
                    stringBuffer.append(" ");
                    stringBuffer.append(op);
                    stringBuffer.append(" ");
                    stringBuffer.append(getNewValue(value));
                    break;

                case ">=":
                    stringBuffer.append(name);
                    stringBuffer.append(" ");
                    stringBuffer.append(op);
                    stringBuffer.append(" ");
                    stringBuffer.append(getNewValue(value));
                    break;

                case "<=":
                    stringBuffer.append(name);
                    stringBuffer.append(" ");
                    stringBuffer.append(op);
                    stringBuffer.append(" ");
                    stringBuffer.append(getNewValue(value));
                    break;

                case "in":
                    getINOrNotIn(stringBuffer, name, op, value);
                    break;
                case "not in":
                    getINOrNotIn(stringBuffer, name, op, value);
                    break;

                case "is null":
                    stringBuffer.append(name);
                    stringBuffer.append(" ");
                    stringBuffer.append(op);
                    break;

                case "is not null":
                    stringBuffer.append(name);
                    stringBuffer.append(" ");
                    stringBuffer.append(op);
                    break;

                case "like":
                    stringBuffer.append(name);
                    stringBuffer.append(" ");
                    stringBuffer.append(op);
                    stringBuffer.append(" ");
                    stringBuffer.append("'" + value.trim() + "'");
                    break;

                case "not link":
                    stringBuffer.append(name);
                    stringBuffer.append(" ");
                    stringBuffer.append(op);
                    stringBuffer.append(" ");
                    stringBuffer.append("'" + value.trim() + "'");
                    break;

                default:
                    LOGGER.debug("操作号 未知:[{}]", op);
                    throw new Exception("操作号 未知:" + op);
            }
            stringBuffer.append(" ");
            stringBuffer.append("and");
        }
        if (stringBuffer == null || stringBuffer.equals("") || stringBuffer.length() <= 3)
            return "empty";
        return stringBuffer.substring(0, stringBuffer.lastIndexOf("a"));
    }


    private static String getNewValue(String value) {
        //判断value  类型

        //mull
        if (value == null)
            return null;

        //mull
        if (value.equals(null))
            return null;

        //mull
        if (value.equals("null"))
            return null;

        // ""
        if (value.equals(""))
            return "''";

        // int
        if (value.trim().matches("^[+-]?[1-9][0-9]*$|^0$"))
            return value;

        // boolean
        if (value.trim().equals("true") || value.trim().equals("false"))
            return value;
        // double
        if (value.trim().matches("^[-]?[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$"))
            return value;

        //日期
        if (value.trim().matches("^((\\\\d{2}(([02468][048])|([13579][26]))[\\\\-\\\\/\\\\s]?((((0?[13578])|(1[02]))[\\\\-\\\\/\\\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\\\-\\\\/\\\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\\\-\\\\/\\\\s]?((0?[1-9])|([1-2][0-9])))))|(\\\\d{2}(([02468][1235679])|([13579][01345789]))[\\\\-\\\\/\\\\s]?((((0?[13578])|(1[02]))[\\\\-\\\\/\\\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\\\-\\\\/\\\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\\\-\\\\/\\\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))"))
            return value;

        return "'" + value + "'";
    }

    private static void getINOrNotIn(StringBuffer stringBuffer, String name, String op, String value) throws Exception {
        if (value == null)
            throw new Exception("The value  null");
        if ("".equals(value))
            throw new Exception("The value  ' ' ");
        if (value.length() < 2)
            throw new Exception("The value length:[" + value.length() + "] error");
        if (!value.startsWith("("))
            throw new Exception("The value:[" + value + "] left  missing  '(' ");
        if (!value.endsWith(")"))
            throw new Exception("The value:[" + value + "] right missing  ')' ");

        String str = value.substring(1, value.length() - 1); //去除 前后 “（ ）”
        StringBuffer newValue = new StringBuffer();

        if (!str.contains(",")) {//如果参数小于2
            newValue.append("(");
            newValue.append(getNewValue(str));
            newValue.append(")");
        } else {//如果有 "," 说明参数大于一个
            for (String s : str.split(",")) {
                String sNew = getNewValue(s);
                if ("".equals(sNew)) {
                    sNew = "''";
                }
                newValue.append(sNew);
                newValue.append(",");
            }
            if(newValue.length()==0){//没有值
                newValue.append("''");
            }else {
                newValue = newValue.deleteCharAt(newValue.lastIndexOf(","));
            }
            newValue.insert(0, "(");
            newValue.append(")");
        }
        stringBuffer.append(name);
        stringBuffer.append(" ");
        stringBuffer.append(op);
        stringBuffer.append(" ");
        stringBuffer.append(newValue);
    }
}