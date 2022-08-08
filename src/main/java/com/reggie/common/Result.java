package com.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果
 * @param <T>
 */

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
    private Map map = new HashMap(); //动态数据
    public static <T> Result<T> success(T object) {
        Result<T> res = new Result<T>();
        res.data = object;
        res.code = 1;
        return res;
    }


    public static <T> Result<T> error(String msg) {
        Result res = new Result();
        res.msg = msg;
        res.code = 0;
        return res;
    }

    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
