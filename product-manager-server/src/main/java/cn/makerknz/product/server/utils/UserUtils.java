package cn.makerknz.product.server.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 用户相关工具类
 * @Author: maker_knz
 * @Date: 2019/7/16/016 15:59
 * @Version 1.0
 */

public class UserUtils {

    /**
     * 如果用户是通过手机号注册，生成一个随机的用户名
     * @return
     */
    public static String autoCreateNickname() {
        String prefix = "maker_";
        String hashCodeV = RandomStringUtils.randomNumeric(8);
        return prefix + hashCodeV;
    }

    /**
     * 如果用户是通过手机号注册，生成一个随机的用户名
     * @return
     */
    public static String autoCreateUsername() {
        String prefix = "maker_";
        String hashCodeV = RandomStringUtils.randomNumeric(8);
        return prefix + hashCodeV;
    }

    /**
     * UUID生成的内容会出现-，此函数是为了删除-
     * @return
     */
    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }

    /**
     * 将字符串解析为yyyy-MM-dd格式的日期
     * @param format
     * @return
     */
    public static Date createDateFromString(String format) {
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd")).parse("1980-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

}
