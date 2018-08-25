package cn.org.upthink.init;

import cn.org.upthink.entity.Role;
import cn.org.upthink.mapper.RoleMapper;
import cn.org.upthink.model.init.SystemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Copyright (C), 2018-2018
 * FileName: SystemDataInit
 * Author: Connie
 * Date: 2018/8/25 14:40
 * Description:
 */
@Component
public class SystemBaseDataInitialization implements CommandLineRunner {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void run(String... args) throws Exception {
        List<Role> roleList = roleMapper.findList(new Role());
        SystemData.INSTANCE.setRoleList(roleList);
    }
}
