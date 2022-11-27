package com.xgksyjxpt.xgksyjxpt.util;


import java.util.Random;

/**
 * 密码工具类
 *
 *
 */
public class PasswordUtils {
    private static final int MIN_NUMBER = 33;
    private static final int MAX_NUMBER = 126;
    private static final int BOUND = MAX_NUMBER - MIN_NUMBER + 1;

    /**
     * 随机生成指定位数的密码
     *
     * char[33,126]，可表示数字、大小写字母、特殊字符
     *
     * @param length 密码长度
     * @return
     */
    public static String randomPassword(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char value = (char) (random.nextInt(BOUND) + MIN_NUMBER);
            builder.append(value);
        }
        return builder.toString();
    }

    /**
     * 获取随机字符串 0-9,a-z,0-9
     * 有两遍0-9，增加数字概率
     * @param length	长度
     * @return
     */
    public static String getLowerLetterNumber(int length) {
        String str = "0123456789abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            sb.append(str.charAt(random.nextInt(46)));
        }
        return sb.toString();
    }


}