package com.example.wmj.bytheway.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by st4rlight on 2018/1/7.
 */

public class GetTime {
    public static String getTime(){
        Date time = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(time);
    }

}
