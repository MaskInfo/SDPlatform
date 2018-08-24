package cn.org.upthink.service;

import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.QuestionMapper;
import cn.org.upthink.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class QuestionService extends BaseCrudService<QuestionMapper, Question> {

    @Transactional(readOnly = false)
    public Page<Question> findPage(Page<Question> page, Question question) {
    return super.findPage(page, question);
    }

    @Transactional(readOnly = false)
    public void save(Question question) {
        // 如果没有审核权限，则将当前内容改为待审核状态
        /*if (!UserUtils.getSubject().isPermitted("school:question:audit")){
            question.setDelFlag(Question.DEL_FLAG_AUDIT);
        }*/
        //question.setUpdateBy(UserUtils.getUser());
        question.setUpdateDate(new Date());
        if (StringUtils.isBlank(question.getId())){
            question.preInsert();
            dao.insert(question);
        }else{
            question.preUpdate();
            dao.update(question);
        }
    }

    @Transactional(readOnly = false)
    public void delete(Question question) {
        super.delete(question);
    }

}
