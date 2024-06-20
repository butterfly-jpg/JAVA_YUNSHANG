package com.atguigu.auth.controller;

import com.atguigu.auth.mapper.SysUserMapper;
import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.config.exception.GuiguException;
import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.utils.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 程志琨
 * @Description: 后台登陆管理
 * @Date: 2024/4/16 22:46
 * @Version: 1.0
 */

@RestController
@RequestMapping("/admin/system/index")
public class IndexController {


    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 登录
     * @return
     */
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
        //获取客户端用户的用户名
        String username = loginVo.getUsername();
        //查询用户表sys_user表中的该用户信息
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(wrapper);
        //判断查询到的sysUser是否为Null
        if(sysUser == null){
            throw new GuiguException(201,"用户名错误");
        }
        //判断查询到的sysUser的密码是否正确
        if(!MD5.encrypt(loginVo.getPassword()).equals(sysUser.getPassword())){
            throw new GuiguException(201,"密码错误");
        }
        //判断该用户是否禁用status为1表示可用0表示禁用
        if(sysUser.getStatus().intValue() != 1){
            throw new GuiguException(201,"用户已被禁用");
        }
        //将用户userId和username一起加密成token
        String token = JwtHelper.createToken(sysUser.getId(), username);
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }


    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        //从请求头中获取token
        String token = request.getHeader("token");
        //从token中获取用户ID
        Long userId = JwtHelper.getUserId(token);
        //查询用户表得到用户信息
        SysUser sysUser = sysUserService.getById(userId);
        //通过用户id查询菜单表获取用户拥有的菜单权限
        List<RouterVo> sysUserRouterList = sysMenuService.findSysUserRouterByUserId(userId);
        //通过用户id查询菜单表获取用户拥有的按钮权限
        List<String> sysUserPermsList = sysMenuService.findSysUserPermsByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        //map.put("roles","[admin]");
        map.put("name",sysUser.getName());
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        //返回用户拥有的菜单权限
        map.put("routers",sysUserRouterList);
        //返回用户拥有的按钮权限
        map.put("buttons",sysUserPermsList);

        return Result.ok(map);
    }


    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}
