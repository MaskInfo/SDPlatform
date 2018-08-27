package cn.org.upthink.service;

import cn.org.upthink.entity.Role;
import cn.org.upthink.entity.User;
import cn.org.upthink.exception.BussinessException;
import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.ResponseConstant;
import cn.org.upthink.model.dto.ExpertFormDTO;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.model.type.RoleTypeEnum;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.ExpertMapper;
import cn.org.upthink.entity.Expert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class ExpertService extends BaseCrudService<ExpertMapper, Expert> {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional(readOnly = false)
    public Page<Expert> findPage(Page<Expert> page, Expert expert) {
    return super.findPage(page, expert);
    }

    @Transactional(readOnly = false)
    public void save(Expert expert) {
        if (StringUtils.isBlank(expert.getId())){
            expert.preInsert();
            dao.insert(expert);
        }else{
            expert.preUpdate();
            dao.update(expert);
        }
    }

    @Transactional(readOnly = false)
    public void delete(Expert expert) {
        super.delete(expert);
    }

    @Transactional(readOnly = false)
    public void apply(ExpertFormDTO expertFormDTO, HttpServletRequest request) {
        UserFormDTO userInfo = LoginTokenHelper.INSTANCE.getUserInfo(stringRedisTemplate, request);

        Expert expert = new Expert();
        expert.setUserId(userInfo.getUserId());

        Expert e = dao.getByUserId(expert);
        if(Objects.nonNull(e)){
            throw new BussinessException(ResponseConstant.EXPERT_IS_EXISTED.getCode(), ResponseConstant.EXPERT_IS_EXISTED.getMsg());
        }

        BeanUtils.copyProperties(expertFormDTO, expert, "userId","state");
        this.save(expert);

        /*User user = new User();
        BeanUtils.copyProperties(userInfo, user);
        Role role = RoleService.getRole(RoleTypeEnum.EXPERT);
        user.setRole(role);
        userService.insertUser_Role(user);*/

    }
}
