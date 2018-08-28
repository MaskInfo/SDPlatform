package cn.org.upthink.service;

import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.CourseDirMapper;
import cn.org.upthink.entity.CourseDir;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class CourseDirService extends BaseCrudService<CourseDirMapper, CourseDir> {

    @Transactional(readOnly = false)
    public Page<CourseDir> findPage(Page<CourseDir> page, CourseDir courseDir) {
    return super.findPage(page, courseDir);
    }

    @Transactional(readOnly = false)
    public void save(CourseDir courseDir) {
        // 如果没有审核权限，则将当前内容改为待审核状态
        /*if (!UserUtils.getSubject().isPermitted("school:courseDir:audit")){
            courseDir.setDelFlag(CourseDir.DEL_FLAG_AUDIT);
        }*/
        //courseDir.setUpdateBy(UserUtils.getUser());
        courseDir.setUpdateDate(new Date());
        if (StringUtils.isBlank(courseDir.getId())){
            courseDir.preInsert();
            dao.insert(courseDir);
        }else{
            courseDir.preUpdate();
            dao.update(courseDir);
        }
    }

    @Transactional(readOnly = false)
    public void delete(CourseDir courseDir) {
        super.delete(courseDir);
    }

}
