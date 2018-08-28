package cn.org.upthink.mapper;

import cn.org.upthink.entity.Course;
import cn.org.upthink.persistence.mybatis.dao.CrudDao;
import cn.org.upthink.persistence.mybatis.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface CourseMapper extends CrudDao<Course> {

    void bind(@Param("userId") String userId, @Param("courseId") String courseId);
}