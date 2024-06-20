package com.atguigu.common.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 程志琨
 * @Description: 自定义异常类
 * @Date: 2024/5/24 21:20
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuiguException extends RuntimeException{

    Integer code;
    String msg;

}
