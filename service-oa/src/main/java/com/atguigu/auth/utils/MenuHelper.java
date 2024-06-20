package com.atguigu.auth.utils;

import com.atguigu.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 程志琨
 * @Description:
 * @Date: 2024/5/23 9:42
 * @Version: 1.0
 */
public class MenuHelper {
    public static List<SysMenu> buildTrees(List<SysMenu> sysMenuList) {
        List<SysMenu> sysMenus = new ArrayList<>();
        for(SysMenu sysMenu:sysMenuList){
            //递归入口进入为parentId=0
            if(sysMenu.getParentId().longValue() == 0){
                sysMenus.add(MenuHelper.getChildren(sysMenu, sysMenuList));
            }
        }
        return  sysMenus;
    }

    public static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> sysMenuList){

        sysMenu.setChildren(new ArrayList<SysMenu>());
        for(SysMenu it : sysMenuList){
            if(sysMenu.getId().longValue() == it.getParentId().longValue()){
                if(sysMenu.getChildren() == null){
                    sysMenu.setChildren(new ArrayList<SysMenu>());
                }
                sysMenu.getChildren().add(MenuHelper.getChildren(it, sysMenuList));
            }
        }
        return sysMenu;
    }
}
