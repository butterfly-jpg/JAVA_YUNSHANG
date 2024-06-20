package com.atguigu.model.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseEntity implements Serializable {

    //@TableId主键注解，使用位置：实体类主键字段，IdType.AUTO为数据库 ID 自增 (mysql配置主键自增长)
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableLogic //逻辑删除字段注解，默认 逻辑删除值为 1 未逻辑删除 0
    @TableField("is_deleted")
    private Integer isDeleted;

    @TableField(exist = false) //exist属性默认值为true，表示是否为数据库字段
    private Map<String,Object> param = new HashMap<>();
}
