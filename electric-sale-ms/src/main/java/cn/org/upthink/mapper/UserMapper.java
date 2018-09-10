package cn.org.upthink.mapper;

import cn.org.upthink.entity.User;
import cn.org.upthink.persistence.mybatis.dao.CrudDao;
import cn.org.upthink.persistence.mybatis.annotation.MyBatisDao;

@MyBatisDao
public interface UserMapper extends CrudDao<User> {

    void insertUser_Role(User user);

    User getByOpenId(String openId);
    User find(User user);
}