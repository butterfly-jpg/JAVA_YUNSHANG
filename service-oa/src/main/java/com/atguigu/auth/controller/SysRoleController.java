package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysRoleService;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.atguigu.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: 程志琨
 * @Description: 管理角色控制层实现类
 * @Date: 2024/4/15 17:16
 * @Version: 1.0
 */
//"角色管理"
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    //根据用户ID userId来获取角色的数据
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
        //调用service层方法查询，结果放入map集合中
        Map<String, Object> roleMap = sysRoleService.findRoleByUserId(userId);
        return Result.ok(roleMap);
    }

    //根据用户分配角色
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }




    //"获取全部角色列表"
    @GetMapping("/findAll")
    public Result<List<SysRole>> findAll(){
        List<SysRole> roleList = sysRoleService.list();
        return Result.ok(roleList);
    }

    //条件分页查询
    //page 当前页  limit 每页显示记录数
    //思路分析，将传入的page和limit放入page对象实现：传递分页相关参数
    //判断sysRoleQueryVo是否为空来决定是否进行条件封装拼接
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo){

        //1 创建Page对象，传递分页相关参数
        Page<SysRole> pageParam = new Page<>(page, limit);

        //2 封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(sysRoleQueryVo.getRoleName())){
            wrapper.like(SysRole::getRoleName, sysRoleQueryVo.getRoleName());
        }

        //3 调用方法实现
        Page<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);
        return Result.ok(pageModel);
    }

    //根据ID查询
    @GetMapping("get/{id}")
    public Result get(@PathVariable Integer id){
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }


    //实现增删改的功能方法
    //新增角色post请求格式+传入参数为JSON @RequestBody
    @PostMapping("save")
    public Result save(@RequestBody SysRole sysRole){
        boolean save = sysRoleService.save(sysRole);
        System.out.println("save = " + save);
        return Result.ok(null);
    }

    //删除角色 delete请求格式+根据id删除角色，传入参数id采用路径传参@PathVariable
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Integer id){
        boolean b = sysRoleService.removeById(id);
        System.out.println("save = " + b);
        return Result.ok(null);
    }

    //根据id列表删除 请求参数设置为List集合, 采用路径传参@RequestBody
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Integer> idList){
        boolean b = sysRoleService.removeByIds(idList);
        System.out.println("save = " + b);
        return Result.ok(null);
    }


    //修改角色 请求格式为Put,传入参数为实体类sysRole采用JSON格式传参
    @PutMapping("update")
    public Result updateById(@RequestBody SysRole sysRole){
        boolean b = sysRoleService.updateById(sysRole);
        System.out.println("save = " + b);
        return Result.ok(null);
    }




}
