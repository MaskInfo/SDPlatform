package cn.org.upthink.model.init;

import cn.org.upthink.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Copyright (C), 2018-2018
 * FileName: SystemData
 * Author: Connie
 * Date: 2018/8/25 14:38
 * Description:
 */
public enum SystemData {
    INSTANCE;

    private List<Role> roleList;

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Role> getRoleList() {
        return roleList;
    }
}
