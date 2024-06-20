package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysMenuMapper;
import com.atguigu.auth.mapper.SysRoleMenuMapper;
import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.utils.MenuHelper;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.vo.system.AssginMenuVo;
import com.atguigu.vo.system.MetaVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 程志琨
 * @Description:
 * @Date: 2024/5/9 10:19
 * @Version: 1.0
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    /**
     * @Author
     * @Date
     * @Description //获取菜单(查询)
     * @Param
     * @Return
     * @Since version 1.0
     */
    @Override
    public List<SysMenu> findNodes() {
        //获取全部菜单数据
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(null);
        if(CollectionUtils.isEmpty(sysMenuList)){
            return null;
        }
        List<SysMenu> sysMenuTree = MenuHelper.buildTrees(sysMenuList);
        return sysMenuTree;
    }

    @Override
    public void removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        Integer integer = baseMapper.selectCount(wrapper);
        if(integer > 0){
            System.out.println("菜单不能删除");
            throw new RuntimeException();
        }
        baseMapper.deleteById(id);
    }

    /**
     * @Author
     * @Date
     * @Description //实现查询所有菜单和角色分配菜单功能
     * @Param
     * @Return
     * @Since version 1.0
     */

    @Override
    public List<SysMenu> findMenusByRoleId(Long roleId) {
        LambdaQueryWrapper<SysMenu> allSysMenusWrapper = new LambdaQueryWrapper<>();
        allSysMenusWrapper.eq(SysMenu::getStatus, 1);
        List<SysMenu> allSysMenus = baseMapper.selectList(allSysMenusWrapper);

        //查 sys_role_menu 表中角色id所对应的所有菜单id
        LambdaQueryWrapper<SysRoleMenu> allSysMenusByRoleId = new LambdaQueryWrapper<>();
        allSysMenusByRoleId.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> allSysRoleMenuList = sysRoleMenuMapper.selectList(allSysMenusByRoleId);

        //从集合allSysRoleMenuList遍历得到所有的SysMenuId
        List<Long> allSysMeunIdList = allSysRoleMenuList.stream().map(c -> c.getMenuId()).collect(Collectors.toList());

        allSysMenus.forEach(item -> {
            if(allSysMeunIdList.contains(item.getId())){
                item.setSelect(true);
            } else{
                item.setSelect(false);
            }
        });

        //将查询结果完毕的allSysMenus集合按照树形结构封装
        List<SysMenu> sysMenus = MenuHelper.buildTrees(allSysMenus);

        return sysMenus;
    }


    /**
     * @Author
     * @Date
     * @Description 根据角色分配菜单功能
     * @Param
     * @Return
     * @Since version 1.0
     */

    @Transactional
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, assginMenuVo.getRoleId());
        sysRoleMenuMapper.delete(wrapper);

        for(Long menuId : assginMenuVo.getMenuIdList()){
            if(StringUtils.isEmpty(menuId)){
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuMapper.insert(sysRoleMenu);
        }
    }

    /**
     * @Author
     * @Date
     * @Description 通过用户id查询菜单表获取用户拥有的菜单权限
     * @Param userId
     * @Return List<RouterVo>
     * @Since version 1.0
     */

    @Override
    public List<RouterVo> findSysUserRouterByUserId(Long userId) {
        //规定用户id为1可以获取所有菜单权限列表
        List<SysMenu> sysRouterList = null;
        if(userId == 1){
            //超级用户id=1直接从baseMapper中查询全部的菜单权限
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysRouterList = baseMapper.selectList(wrapper);
        } else {
            //其他用户根据多表联查获取各自的菜单权限列表
            //多表联查:用户角色关系表、 角色菜单关系表、 菜单表
            //（1）查询单表可以使用mybatis-plus  （2）多表联查只能使用mybatis，手写sql语句查询
            sysRouterList = sysMenuMapper.findUserRouterList(userId);
        }

        //将菜单权限列表sysRouterList转成前端模板要求格式
        //先转成树形结构
        List<SysMenu> sysRouterTreeList = MenuHelper.buildTrees(sysRouterList);
        List<RouterVo> routerList = this.buildRouterList(sysRouterTreeList);
        return routerList;
    }

    //将菜单权限列表sysRouterTreeList转成前端模板要求格式
    private List<RouterVo> buildRouterList(List<SysMenu> sysRouterTreeList) {
        List<RouterVo> routerVoList = new ArrayList<>();
        for(SysMenu sysMenu : sysRouterTreeList){
            RouterVo routerVo = new RouterVo();
            routerVo.setPath(getRouterPath(sysMenu));
            routerVo.setComponent(sysMenu.getComponent());
            routerVo.setHidden(false);
            routerVo.setAlwaysShow(false);
            routerVo.setMeta(new MetaVo(sysMenu.getName(), sysMenu.getIcon()));
            //下一层数据
            List<SysMenu> children = sysMenu.getChildren();
            if(sysMenu.getType().intValue() == 1){
                List<SysMenu> hiddenMenuList =
                        children.stream().filter(item -> !StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
                for(SysMenu hiddenMenu : hiddenMenuList){
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routerVoList.add(hiddenRouter);
                }
            }else {
                if (!CollectionUtils.isEmpty(children)) {
                    if(children.size() > 0) {
                        routerVo.setAlwaysShow(true);
                    }
                    routerVo.setChildren(buildRouterList(children));
                }
            }
            routerVoList.add(routerVo);

        }

        return routerVoList;

    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    /**
     * @Author
     * @Date
     * @Description 通过用户id查询菜单表获取用户拥有的按钮权限
     * @Param userId
     * @Return List<String>
     * @Since version 1.0
     */

    @Override
    public List<String> findSysUserPermsByUserId(Long userId) {
        //假设用户Id为1就可以拥有所有按钮权限
        List<SysMenu> sysRouterList = null;
        if(userId.longValue() == 1){
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            sysRouterList = baseMapper.selectList(wrapper);
        } else {
            //其他用户根据多表联查获取各自的菜单权限列表
            //多表联查:用户角色关系表、 角色菜单关系表、 菜单表
            sysRouterList = sysMenuMapper.findUserRouterList(userId);
        }

        List<String> permsList =
                sysRouterList.stream().filter(item -> item.getType() == 2).map(item ->
                        item.getPerms()).collect(Collectors.toList());
        //遍历sysRouterList获取所有的perms
//        for(SysMenu sysMenu : sysRouterList){
//            if(sysMenu.getType() == 2){
//                permsList.add(sysMenu.getPerms());
//            }
//        }

        return permsList;
    }







}




