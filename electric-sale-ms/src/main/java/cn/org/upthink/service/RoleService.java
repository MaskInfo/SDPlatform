package cn.org.upthink.service;

import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.RoleMapper;
import cn.org.upthink.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class RoleService extends BaseCrudService<RoleMapper, Role> {

    @Transactional(readOnly = false)
    public Page<Role> findPage(Page<Role> page, Role role) {
    return super.findPage(page, role);
    }

    @Transactional(readOnly = false)
    public void save(Role role) {
        // 如果没有审核权限，则将当前内容改为待审核状态
        /*if (!UserUtils.getSubject().isPermitted("school:role:audit")){
            role.setDelFlag(Role.DEL_FLAG_AUDIT);
        }*/
        //role.setUpdateBy(UserUtils.getUser());
        role.setUpdateDate(new Date());
        if (StringUtils.isBlank(role.getId())){
            role.preInsert();
            dao.insert(role);
        }else{
            role.preUpdate();
            dao.update(role);
        }
    }

    @Transactional(readOnly = false)
    public void delete(Role role) {
        super.delete(role);
    }

}
