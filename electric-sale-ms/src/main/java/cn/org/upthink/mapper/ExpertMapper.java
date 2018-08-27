package cn.org.upthink.mapper;

import cn.org.upthink.entity.Expert;
import cn.org.upthink.persistence.mybatis.dao.CrudDao;
import cn.org.upthink.persistence.mybatis.annotation.MyBatisDao;

@MyBatisDao
public interface ExpertMapper extends CrudDao<Expert> {

}