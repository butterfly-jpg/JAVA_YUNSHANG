package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.vo.system.AssginMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 程志琨
 * @Description: 系统管理之菜单管理的Controller层
 * @Date: 2024/5/9 10:11
 * @Version: 1.0
 */

@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    //实现查询所有菜单和
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId){
        List<SysMenu> sysMenus = sysMenuService.findMenusByRoleId(roleId);
        return Result.ok(sysMenus);
    }

    //根据角色分配菜单功能
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuVo assginMenuVo){
        sysMenuService.doAssign(assginMenuVo);
        return Result.ok();
    }


    //实现增删改查功能
    //获取菜单(查询)
    @GetMapping("findNodes")
    public Result findNodes(){
        List<SysMenu> nodes = sysMenuService.findNodes();
        return Result.ok(nodes);
    }
    //保存菜单(增加)
    @PostMapping("save")
    public Result save(@RequestBody SysMenu sysMenu){
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    //修改菜单(修改)
    @PutMapping("update")
    public Result update(@RequestBody SysMenu sysMenu){
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    //删除菜单(删除)
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }

}
