package com.atguigu.auth.controller;

import com.atguigu.auth.mapper.SysUserMapper;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.result.Result;
import com.atguigu.common.utils.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.SysUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @Author: 程志琨
 * @Description:
 * @Date: 2024/4/23 22:42
 * @Version: 1.0
 */

@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    //修改用户状态
    //用户状态：状态（1：正常 0：停用），当用户状态为正常时，可以访问后台系统，当用户状态停用后，不可以登录后台系统
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status){
        sysUserService.updateStatus(id, status);
        return Result.ok();
    }



    //用户条件分页查询
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        SysUserQueryVo sysUserQueryVo){

        //创建page对象
        Page<SysUser> pageParam = new Page<>(page, limit);

        //封装条件，判断条件值不为空
        //从条件对象sysUserQueryVo中获取条件值
        String keyword = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();


        //只能通过用户名username来查询数据
        if(!StringUtils.isEmpty(keyword)){
            wrapper.like(SysUser::getUsername, keyword);
        }

        if(!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge(SysUser::getCreateTime, createTimeBegin);
        }

        if(!StringUtils.isEmpty(createTimeEnd)){
           wrapper.le(SysUser::getCreateTime, createTimeEnd);
        }


        //调用mp的方法实现条件分页查询
        Page<SysUser> pageModel = sysUserService.page(pageParam, wrapper);

        return Result.ok(pageModel);
    }

    //根据id获取用户
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    //保存用户
    @PostMapping("save")
    public Result save(@RequestBody SysUser sysUser){
        //将客户端的用户密码加密成MD5格式存入数据库
        sysUser.setPassword(MD5.encrypt(sysUser.getPassword()));
        sysUserService.save(sysUser);
        return Result.ok();
    }

    //更新用户
    @PutMapping("update")
    public Result update(@RequestBody SysUser sysUser){
        sysUserService.updateById(sysUser);
        return Result.ok();
    }

    //根据id删除用户
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        sysUserService.removeById(id);
        return Result.ok();

    }

}
