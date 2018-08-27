package cn.org.upthink.service;

import cn.org.upthink.entity.User;
import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.dto.QuestionQueryDTO;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.QuestionMapper;
import cn.org.upthink.entity.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class QuestionService extends BaseCrudService<QuestionMapper, Question> {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional(readOnly = false)
    public Page<Question> findPage(Page<Question> page, Question question) {
    return super.findPage(page, question);
    }

    @Transactional(readOnly = false)
    public void save(Question question) {
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

    @Transactional(readOnly = false)
    public Page<Question> list(QuestionQueryDTO questionQueryDTO, HttpServletRequest request, HttpServletResponse response) {
        UserFormDTO userInfo = LoginTokenHelper.INSTANCE.getUserInfo(stringRedisTemplate, request);

        Question question = new Question();
        User user = new User();
        user.setId(userInfo.getUserId());
        question.setQuestioner(user);
        question.setPay(true);

        return this.findPage(new Page<Question>(request, response), question);
    }
}
