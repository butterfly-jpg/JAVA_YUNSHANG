package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.mapper.SysUserRoleMapper;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: 程志琨
 * @Description:
 * @Date: 2024/4/15 16:56
 * @Version: 1.0
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService{

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * @Author
     * @Date
     * @Description
     * 根据用户id来获取角色数据
     *      * 思路分析：
     *      * （1）查询所有的角色
     *      * （2）查询该用户id下的角色
     * @Param
     * @Return
     * @Since version 1.0
     */
    @Override
    public Map<String, Object> findRoleByUserId(Long userId) {

        //查询所有角色信息，放入List<SysRole>集合allRolesList中
        List<SysRole> allRolesList = sysRoleMapper.selectList(null);

        //通过当前用户id userId查询 角色用户关系表sys_user_role 得到该用户id下的所有角色id，放入List<SysUserRole>集合existUserRoleList中
        List<SysUserRole> existUserRoleList =
                sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));

        //从List<SysUserRole>集合existUserRoleList中取出所有的角色roleId，放入到List<Long>集合existRoleIdList中
        List<Long> existRoleIdList = existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        //想获取List<Long>集合中角色id的信息：
        //通过遍历 包含所有角色信息的List<SysRole>集合allRolesList 把匹配到的角色role信息存入到List<SysRole>集合assginRoleList中
        //这样就从包含所有角色信息的List<SysRole>集合中筛选出当前用户id下的角色信息
        List<SysRole> assginRoleList = new ArrayList<>();
        for(SysRole sysRole : allRolesList){
            if(existRoleIdList.contains(sysRole.getId())){
                assginRoleList.add(sysRole);
            }
        }


        //把得到的两部分数据（所有角色信息集合List<SysRole>，用户id对应下的所有角色id集合List<SysRole>）封装到map集合然后返回
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assginRoleList);
        roleMap.put("allRolesList", allRolesList);

        return roleMap;
    }

    /**
     * @Author 
     * @Date
     * @Description 根据用户分配角色
     *
     * * (1)把用户之前分配的角色先删掉，在用户角色关系表中通过userId删除
     *      * (2)保存最新分配角色
     * @Param 
     * @Return 
     * @Since version 1.0
     */
    
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {

        //把用户之前分配的角色先删掉，在用户角色关系表中通过userId删除
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<SysUserRole>();
        wrapper.eq(SysUserRole::getUserId, assginRoleVo.getUserId());
        sysUserRoleMapper.delete(wrapper);

        //保存最新分配角色
        for (Long roleId : assginRoleVo.getRoleIdList()){
            if(StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleMapper.insert(sysUserRole);
        }

    }
}
