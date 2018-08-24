package cn.org.upthink.service;

import cn.org.upthink.exception.BussinessException;
import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.ResponseConstant;
import cn.org.upthink.model.dto.UserFormDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class UserService extends BaseCrudService<UserMapper, User> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;

    @Value("${wechat.loginUrl}")
    private String loginUrl;

    @Transactional(readOnly = false)
    public Page<User> findPage(Page<User> page, User user) {
    return super.findPage(page, user);
    }

    @Transactional(readOnly = false)
    public void saveOrUpdate(User user) {
        if(Objects.nonNull(user.getId())){
            user.preUpdate();
            dao.update(user);
        }else{
            user.preInsert();
            dao.insert(user);
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
        if (ret.containsKey("errcode")) {
            throw new BussinessException(ResponseConstant.GET_OPENID_FAIL.getCode(), ResponseConstant.GET_OPENID_FAIL.getMsg());
        }
        String session_key = (String) ret.get("session_key");
        String openid = (String) ret.get("openid");

        //保存用户信息
        User user = new User();
        user.setOpenId(openid);
        User u = userMapper.get(user);
        if(Objects.nonNull(u)){
            user = u;
        }
        BeanUtils.copyProperties(userFormDTO, user, "openId");

        //返回accessToken
        return LoginTokenHelper.setSession(session_key, openid);

    }
}
