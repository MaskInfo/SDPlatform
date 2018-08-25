package cn.org.upthink.service;

import cn.org.upthink.entity.Role;
import cn.org.upthink.entity.User;
import cn.org.upthink.helper.LoginTokenHelper;
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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExpertService extends BaseCrudService<ExpertMapper, Expert> {

    @Autowired
    private UserService userService;

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

    public void apply(ExpertFormDTO expertFormDTO, HttpServletRequest request) {
        User userInfo = LoginTokenHelper.getUserInfo(request);

        Expert expert = new Expert();
        expert.setUserId(userInfo.getId());
        BeanUtils.copyProperties(expertFormDTO, expert, "userId","state");
        this.save(expert);

        Role role = RoleService.getRole(RoleTypeEnum.NORMAL);
        userInfo.setRole(role);
        userService.insertUser_Role(userInfo);

    }
}
