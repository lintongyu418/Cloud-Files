package com.example.core.utils;

import com.example.core.configs.Constant;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R {

    private boolean success;
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    private R() {
    }

    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(Constant.SUCCESS_RESPONSE);
        r.setMessage(Constant.R_SUCCESS);
        return r;
    }

    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(Constant.ERROR_UNKNOWN);
        r.setMessage(Constant.R_ERROR);
        return r;
    }

    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }
}
