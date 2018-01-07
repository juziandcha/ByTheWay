package com.example.wmj.bytheway.Util;

import com.example.wmj.bytheway.Activities.ByTheWayActivity;
import com.example.wmj.bytheway.ConnSup.JsonContent;

import org.json.JSONObject;

/**
 * Created by st4rlight on 2018/1/4.
 */

public class GetData {
    public static void runGetData(String sql, String type, JSONObject keyValue) throws Exception {
        if (ByTheWayActivity.session.isConnected()) {
            try {
                JsonContent JsonCont = new JsonContent(sql, type, keyValue);
                ByTheWayActivity.session.write(JsonCont.getJsonStr());
                ByTheWayActivity.lock.lock();
                ByTheWayActivity.condition.await();
                ByTheWayActivity.lock.unlock();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            throw new Exception();
        }
    }
}
