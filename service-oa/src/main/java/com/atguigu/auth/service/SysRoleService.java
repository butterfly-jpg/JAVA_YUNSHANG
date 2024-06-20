package com.atguigu.auth.service;

import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    /**
     * @Author
     * @Date
     * @Description 根据用户id来获取角色数据
     * 思路分析：
     * （1）查询所有的角色
     * （2）查询该用户id下的角色
     * @Param
     * @Return
     * @Since version 1.0
     */

    Map<String, Object> findRoleByUserId(Long userId);


    /**
     * @Author
     * @Date
     * @Description
     * (1)把用户之前分配的角色先删掉，在用户角色关系表中通过userId删除
     * (2)保存最新分配角色
     * @Param
     * @Return
     * @Since version 1.0
     */

    void doAssign(AssginRoleVo assginRoleVo);
}
