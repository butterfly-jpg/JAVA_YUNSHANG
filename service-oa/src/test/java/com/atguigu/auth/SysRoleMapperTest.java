package com.atguigu.auth;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: 程志琨
 * @Description:
 * @Date: 2024/4/15 16:28
 * @Version: 1.0
 */
@SpringBootTest
public class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    public void testSelectList(){
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        System.out.println("sysRoles = " + sysRoles);
    }


}
