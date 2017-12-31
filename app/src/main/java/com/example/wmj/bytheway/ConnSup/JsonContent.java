package com.example.wmj.bytheway.ConnSup;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by st4rlight on 2017/12/22.
 */

public class JsonContent {
    private String JsonSql;
    private String JsonSqlType;//type: update, query
    private JSONObject keyValue;

    public JsonContent(String jsonSql, String jsonSqlType, JSONObject prepareStmt) {
        JsonSql = jsonSql;
        JsonSqlType = jsonSqlType;
        keyValue = prepareStmt;
    }

    public String getJsonStr(){
        JSONObject json=new JSONObject();
        if (keyValue==null)
            keyValue=new JSONObject();
        try {
            json.put("sql", JsonSql);
            json.put("type", JsonSqlType);
            json.put("keyValue",keyValue);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

    public String getJsonSql() {
        return JsonSql;
    }

    public String getJsonSqlType() {
        return JsonSqlType;
    }

    public void setJsonSql(String jsonSql) {
        JsonSql = jsonSql;
    }

    public void setJsonSqlType(String jsonSqlType) {
        JsonSqlType = jsonSqlType;
    }
}
