package models.utils;

import play.mvc.Http.Request;
import play.mvc.Scope;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * String 工具类
 *
 * @author
 */
public class StringHelper {


    /**
     *  将ResultSet转为List
      */
    public static List resultSetToList(ResultSet rs) throws java.sql.SQLException {
        if (rs == null)
            return Collections.EMPTY_LIST;
        ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等
        int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数
        List list = new ArrayList();
        Map rowData = new HashMap();
        while (rs.next()) {
            rowData = new HashMap(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
           // System.out.println("list:" + list.toString());
        }
        return list;
    }



    /**
     * 将String转换为Integer类型的List
     *
     * @param str
     * @param split
     * @return
     */
    public final static List<Integer> toIntegerList(String str, String split) {
        List<Integer> ret = new ArrayList<Integer>();
        if (str != null && !str.isEmpty()) {
            String[] strArray = str.split(split);

            for (String item : strArray) {
                if (!item.isEmpty())
                    ret.add(Integer.parseInt(item));
            }
        }
        return ret;
    }

    /**
     * MD5字符串加密
     *
     * @param str
     * @return
     * @throws java.security.NoSuchAlgorithmException
     */
    public final static String md5(String str) throws NoSuchAlgorithmException {
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] btInput = str.getBytes();
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest md5Inst = MessageDigest.getInstance("MD5");
        // 使用指定的字节更新摘要
        md5Inst.update(btInput);
        // 获得密文
        byte[] bytes = md5Inst.digest();

        StringBuffer strResult = new StringBuffer();
        // 把密文转换成十六进制的字符串形式
        for (int i = 0; i < bytes.length; i++) {
            strResult.append(hexDigits[(bytes[i] >> 4) & 0x0f]);
            strResult.append(hexDigits[bytes[i] & 0x0f]);
        }
        return strResult.toString();
    }


    /**
     * SHA-1字符串加密
     *
     * @param str
     * @return
     * @throws java.security.NoSuchAlgorithmException
     */
    public final static String sha1(String str) throws NoSuchAlgorithmException {
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] btInput = str.getBytes();
        // 获得SHA-1摘要算法的 MessageDigest 对象
        MessageDigest sha1Inst = MessageDigest.getInstance("SHA-1");
        // 使用指定的字节更新摘要
        sha1Inst.update(btInput);
        // 获得密文
        byte[] bytes = sha1Inst.digest();

        StringBuffer strResult = new StringBuffer();
        // 把密文转换成十六进制的字符串形式
        for (int i = 0; i < bytes.length; i++) {
            strResult.append(hexDigits[(bytes[i] >> 4) & 0x0f]);
            strResult.append(hexDigits[bytes[i] & 0x0f]);
        }
        return strResult.toString();
    }

    /**
     * 获取订单流水号  时间（到毫秒）+三位随机数
     *
     * @return
     */
    public static String getOrderNum() {
        try {
            Thread.sleep(1l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmsssss");
        String num = Integer.toString((int) ((999 - 100) * Math.random() + 100));//取得一个100-999的3位随机数字字符串
        String orderNum = format.format(date) + num;
        return orderNum;
    }


    /**
     * 获得用户ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获得用户ip地址
     *
     * @param request
     * @return
     */
    public static String getRequestIpAddr(Request request) {
        String host = request.host;
        String port = request.port.toString();
        String str = ":"+port;

        return host.replaceAll(str,"");
    }

    /**
     * ResultSet转换为List的方法
     * by hfl 2015-8-25
     *
     * @param rs
     * @return
     * @throws java.sql.SQLException
     */
    public static List convertList(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount(); //Map rowData;
        while (rs.next()) { //rowData = new HashMap(columnCount);
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    /**
     * 获得当前的时间转成 字符串 2015-05-12 这种格式 为了保存资讯图片概要路径用
     */
    public static String getStringByNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }




    /**
     * 拼接图片的绝对路径
     */
    public static String getAbsoluteUrl(HttpServletRequest request ){
       //return "http://" + request.getHeader("Host");
      String port = String.valueOf(request.getLocalPort());
        //测试地址
      // return  "http://58.58.180.48:"+port;
        //正式地址
        // return  "http://58.58.180.48:8082";
        return  "http://121.42.157.157:"+port;
    }

    /**
     * date类型数据的加减天数 注意是为了计算 逾期天数 而指定的方法，其他方法引用的化 得在days加1
     */
    public static Date getAddDayDate(Date dt, int days) {
        if (dt == null)
            dt = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + days - 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 将timestamp转换成date
     *
     * @param tt
     * @return
     * @author hellostoy
     */
    public static Date timestampToDate(Timestamp tt) {
        return new Date(tt.getTime());
    }

    /**
     * Date转成Calendar
     */
    public static Calendar dateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return  cal;
    }

    /**
     * 合同编号  eg:RJDZLSW-20160303008
     */
    public static String getContractNo(){
        String contractNo = "RJDZ";
        contractNo += Scope.Session.current().get("userName").toUpperCase();
        contractNo += "-";
        contractNo += SerialNumber.newInstance("",new Date()).toString();

        return contractNo;
    }

    /**
     * 合同编号  eg:RJDZ-20160303008
     */
    public static String getMobileContractNo(){
        String contractNo = "RJDZ";
        contractNo += "-";
        contractNo += SerialNumber.newInstance("",new Date()).toString();

        return contractNo;
    }


    /**
     * 去字符串的空格 后 是否为 空
     */
     public static  boolean isTrimNotNull(String string){
        return  string!=null  && !"".equals(string);
     }













}
