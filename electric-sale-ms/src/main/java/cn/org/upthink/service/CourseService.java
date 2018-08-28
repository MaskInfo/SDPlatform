package cn.org.upthink.service;

import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.CourseMapper;
import cn.org.upthink.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class CourseService extends BaseCrudService<CourseMapper, Course> {

    @Transactional(readOnly = false)
    public Page<Course> findPage(Page<Course> page, Course course) {
    return super.findPage(page, course);
    }

    @Transactional(readOnly = false)
    public void save(Course course) {
        // 如果没有审核权限，则将当前内容改为待审核状态
        /*if (!UserUtils.getSubject().isPermitted("school:course:audit")){
            course.setDelFlag(Course.DEL_FLAG_AUDIT);
        }*/
        //course.setUpdateBy(UserUtils.getUser());
        course.setUpdateDate(new Date());
        if (StringUtils.isBlank(course.getId())){
            course.preInsert();
            dao.insert(course);
        }else{
            course.preUpdate();
            dao.update(course);
        }
    }

    @Transactional(readOnly = false)
    public void delete(Course course) {
        super.delete(course);
    }

    public void bind(String userId, String courseId) {

        this.dao.bind(userId,courseId);
    }
}
