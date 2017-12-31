package com.example.wmj.bytheway.ConnSup;

import java.security.MessageDigest;

/**
 * Created by st4rlight on 2017/12/22.
 * References https://www.cnblogs.com/yanghuahui/archive/2013/06/16/3139411.html
 */

public class MD5 {

    private static MessageDigest md5 = null;
    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //获取MD5值
    public static String getMD5(String str) {
        byte[] bs = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder(40);
        for(byte x:bs) {
            if((x & 0xff)>>4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }
}