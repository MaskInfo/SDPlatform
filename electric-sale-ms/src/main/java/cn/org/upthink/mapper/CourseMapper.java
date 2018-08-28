package cn.org.upthink.mapper;

import cn.org.upthink.entity.Course;
import cn.org.upthink.persistence.mybatis.dao.CrudDao;
import cn.org.upthink.persistence.mybatis.annotation.MyBatisDao;

@MyBatisDao
public interface CourseMapper extends CrudDao<Course> {

}