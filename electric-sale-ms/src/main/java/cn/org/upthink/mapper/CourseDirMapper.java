package cn.org.upthink.mapper;

import cn.org.upthink.entity.CourseDir;
import cn.org.upthink.persistence.mybatis.dao.CrudDao;
import cn.org.upthink.persistence.mybatis.annotation.MyBatisDao;

@MyBatisDao
public interface CourseDirMapper extends CrudDao<CourseDir> {

}