package cn.org.upthink.mapper;

import cn.org.upthink.entity.Role;
import cn.org.upthink.persistence.mybatis.dao.CrudDao;
import cn.org.upthink.persistence.mybatis.annotation.MyBatisDao;

@MyBatisDao
public interface RoleMapper extends CrudDao<Role> {

}