package cn.org.upthink.mapper;

import cn.org.upthink.entity.Role;
import cn.org.upthink.persistence.mybatis.dao.CrudDao;
import cn.org.upthink.persistence.mybatis.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface RoleMapper extends CrudDao<Role> {

    Role getRoleByUserId(Long userId);

    void bind(@Param("userId") String userId, @Param("roleId") String roleId);
}