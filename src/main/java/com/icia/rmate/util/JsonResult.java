package com.icia.rmate.util;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class JsonResult {

    public enum Code {
        SUCC, FAIL
    }

    private Code code = Code.SUCC;
    private String msg;
    private Map<String, Object> data;

    public void addData(String key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(key, value);
    }
    public JsonResult success() {
        this.code = Code.SUCC;
        return this;
    }
    public static JsonResult fail(Exception e) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.code = Code.FAIL;
        jsonResult.msg = e.getMessage();
        return jsonResult;
    }
}