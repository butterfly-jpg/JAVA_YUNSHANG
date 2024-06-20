package com.atguigu.auth.service;

import com.atguigu.model.system.SysMenu;
import com.atguigu.vo.system.AssginMenuVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SysMenuService extends IService<SysMenu> {

    //获取菜单(查询)
    List<SysMenu> findNodes();
    //删除菜单(删除)
    void removeMenuById(Long id);
    //实现查询所有菜单和角色分配菜单功能
    List<SysMenu> findMenusByRoleId(Long roleId);
    ///根据角色分配菜单功能
    void doAssign(AssginMenuVo assginMenuVo);

    //通过用户id查询菜单表获取用户拥有的菜单权限
    List<RouterVo> findSysUserRouterByUserId(Long userId);

    //通过用户id查询菜单表获取用户拥有的按钮权限
    List<String> findSysUserPermsByUserId(Long userId);
}
