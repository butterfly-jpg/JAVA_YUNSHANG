package com.atguigu.auth.service;

import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysUserService extends IService<SysUser> {

    //修改用户状态
    void updateStatus(Long id, Integer status);


    //通过用户名查询用户信息
    SysUser getByUsername(String username);
}
