package cn.org.upthink.service;

import cn.org.upthink.entity.Role;
import cn.org.upthink.exception.BussinessException;
import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.ResponseConstant;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.model.type.RoleTypeEnum;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.UserMapper;
import cn.org.upthink.entity.User;
import cn.org.upthink.util.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserService extends BaseCrudService<UserMapper, User> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${wechat.loginUrl}")
    private String loginUrl;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional(readOnly = false)
    public Page<User> findPage(Page<User> page, User user) {
    return super.findPage(page, user);
    }

    @Transactional(readOnly = false)
    public void save(User user) {
        if(StringUtils.isBlank(user.getId())){
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

    @Transactional(readOnly = false)
    public String login(String code, UserFormDTO userFormDTO) throws Exception {
        //code -> session-key openId
        JSONObject ret = JSON.parseObject(HttpClientUtils.INSTANCE.sendGet(loginUrl + code));
        System.out.println(JSON.toJSONString(ret));
        if (ret.containsKey("errcode")) {
            throw new BussinessException(ResponseConstant.GET_OPENID_FAIL.getCode(), ResponseConstant.GET_OPENID_FAIL.getMsg());
        }
        String session_key = (String) ret.get("session_key");
        String openid = (String) ret.get("openid");

        //保存用户信息
        User user = this.saveUser(userFormDTO, openid);

        //返回accessToken
        userFormDTO.setUserId(user.getId());
        return LoginTokenHelper.INSTANCE.setSession(stringRedisTemplate, session_key, openid, userFormDTO);

    }

    @Transactional(readOnly = false)
    public User saveUser(UserFormDTO userFormDTO, String openid) {
        User user = new User();
        User dbUser = dao.getByOpenId(openid);
        if(Objects.nonNull(dbUser)){
            BeanUtils.copyProperties(dbUser, user);
        }
        BeanUtils.copyProperties(userFormDTO, user, "openId");
        this.save(user);

        //设置新用户角色
        if(Objects.isNull(dbUser)){
            Role role = RoleService.getRole(RoleTypeEnum.NORMAL);
            List<Role> roleList = new ArrayList();
            roleList.add(role);
            user.setRoles(roleList);
            this.insertUser_Role(user);
        }

        return user;
    }

    @Transactional(readOnly = false)
    public void insertUser_Role(User user){
        dao.insertUser_Role(user);
    }

    @Transactional(readOnly = true)
    public User getUserByOpenId(String openId){
        return dao.getByOpenId(openId);
    }
}
