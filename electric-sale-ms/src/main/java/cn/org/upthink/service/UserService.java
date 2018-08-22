package cn.org.upthink.service;

import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.UserMapper;
import cn.org.upthink.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class UserService extends BaseCrudService<UserMapper, User> {

    @Transactional(readOnly = false)
    public Page<User> findPage(Page<User> page, User user) {
    return super.findPage(page, user);
    }

    @Transactional(readOnly = false)
    public void save(User user) {
        // 如果没有审核权限，则将当前内容改为待审核状态
        /*if (!UserUtils.getSubject().isPermitted("school:user:audit")){
            user.setDelFlag(User.DEL_FLAG_AUDIT);
        }*/
        //user.setUpdateBy(UserUtils.getUser());
        user.setUpdateDate(new Date());
        if (StringUtils.isBlank(user.getId())){
            user.preInsert();
            dao.insert(user);
        }else{
            user.preUpdate();
            dao.update(user);
        }
    }

    @Transactional(readOnly = false)
    public void delete(User user) {
        super.delete(user);
    }

}
