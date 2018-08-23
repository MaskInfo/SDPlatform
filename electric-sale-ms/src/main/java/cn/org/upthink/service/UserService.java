package cn.org.upthink.service;

import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.UserMapper;
import cn.org.upthink.entity.User;
import org.springframework.beans.BeanUtils;
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
    public void save(UserFormDTO userFormDTO) {
        User user = new User();
        user.setOpenId(userFormDTO.getOpenId());
        Page<User> page = findPage(new Page<>(), user);
        BeanUtils.copyProperties(userFormDTO, user);
        if(page.getList() == null || page.getList().size() == 0){
            user.preInsert();
            dao.insert(user);
        }else{
            user = page.getList().get(0);
            user.preUpdate();
            dao.update(user);
        }
    }

    @Transactional(readOnly = false)
    public void delete(User user) {
        super.delete(user);
    }

}
