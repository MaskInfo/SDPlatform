package cn.org.upthink.service;

import cn.org.upthink.model.init.SystemData;
import cn.org.upthink.model.type.RoleTypeEnum;
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

    public static Role getRole(RoleTypeEnum roleType){
        int type = roleType.getType();

        for (Role role : SystemData.INSTANCE.getRoleList()) {
            if(type == role.getRoleType()){
                return role;
            }
        }

        return null;
    }

    /**
     * 绑定角色
     * @param userId
     */
    public void bind(String userId, String roleId) {
        this.dao.bind(userId,roleId);
    }
}
