package com.jwt.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

public record RestBean<T>(int code, T data, String message) {
    // 200 请求成功(有数据)
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, data, "请求成功");
    }
    // 200 请求成功(无数据)
    public static <T> RestBean<T> success() {
        return success(null);
    }
    // 请求失败(有状态码和错误信息)
    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, null, message);
    }
    // 请求失败(有错误信息)
    public static <T> RestBean<T> failure(String message) {
        return failure(500, message);
    }
    // 401 未登录
    public static <T> RestBean<T> unauthorized(String message) {
        return failure(401, message);
    }
    // 403 无权限
    public static <T> RestBean<T> forbidden(String message) {
        return failure(403, message);
    }
    // 转换为 JSON 字符串
    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
