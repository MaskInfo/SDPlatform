package cn.org.upthink.mapper;

import cn.org.upthink.entity.Material;
import cn.org.upthink.persistence.mybatis.dao.CrudDao;
import cn.org.upthink.persistence.mybatis.annotation.MyBatisDao;

@MyBatisDao
public interface MaterialMapper extends CrudDao<Material> {

}