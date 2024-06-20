package com.atguigu.common.result;

import lombok.Getter;

/**
 * @Author: 程志琨
 * @Description: 统一返回结果状态信息类
 * @Date: 2024/4/15 17:30
 * @Version: 1.0
 */

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),

    LOGIN_ERROR(204, "登录认证失败")
    ;

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
