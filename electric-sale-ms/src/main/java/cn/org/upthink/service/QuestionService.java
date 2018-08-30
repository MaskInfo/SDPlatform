package cn.org.upthink.service;

import cn.org.upthink.entity.User;
import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.dto.PayFormDto;
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class QuestionService extends BaseCrudService<QuestionMapper, Question> {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${wechat.perparePayUrl}")
    private String preparePayUrl;

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

    public void save(String operationId, HttpServletRequest request, PayFormDto payFormDto) {
        Question q = new Question();
        q.setId(operationId);
        q.setIsNewRecord(true);
        q.setQuesTitle(payFormDto.getQuesTitle());
        q.setQuesDetail(payFormDto.getQuesDetail());
        q.setCreateDate(new Date());
        q.setUpdateDate(q.getCreateDate());

        User user = new User();
        UserFormDTO userInfo = LoginTokenHelper.INSTANCE.getUserInfo(stringRedisTemplate, request);
        user.setId(userInfo.getUserId());
        q.setQuestioner(user);

        dao.insert(q);
    }
}
