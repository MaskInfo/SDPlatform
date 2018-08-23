package cn.org.upthink.web.controller;

import cn.org.upthink.anno.RequestLogging;
import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.ResponseConstant;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.model.dto.UserQueryDTO;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
import cn.org.upthink.util.HttpClientUtils;
import cn.org.upthink.web.BaseController;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.entity.User;
import cn.org.upthink.service.UserService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rover on 2018-06-08.
 */
@Api(value = "userApi", description = "user的接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1")
public class UserController extends BaseController {

    @Value("${wechat.loginUrl}")
    private String loginUrl;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取user详细信息", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @GetMapping(value = "/user/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> findUser(@PathVariable("id") String id) {
        User user = null;
        try {
            user = userService.get(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            return getBaseResultSuccess(user, "有效对象");
        } else {
            return getBaseResultFail(null, "无效的id，没有获取到对象");
        }
    }

    @ApiOperation(value = "删除User信息", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/user/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> deleteUser(@PathVariable("id") String id) {
        User user = null;
        try {
            user = userService.get(id);
            if (user != null) {
                userService.delete(user);
                return getBaseResultSuccess(true, "已成功删除User对象");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "无效的id，没有删除User对象");
    }

    @ApiOperation(value = "新增User", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/user", produces = "application/json;charset=UTF-8")
    public BaseResult<?> addUser(@ApiParam @RequestBody UserFormDTO userFormDTO) {
        try {
            /*User user = new User();
            user.setHeadImg(userFormDTO.getHeadImg());
            user.setOpenId(userFormDTO.getOpenId());
            user.setNickName(userFormDTO.getNickName());
            user.setUserName(userFormDTO.getUserName());
            userService.save(user);*/
            return getBaseResultSuccess(true, "保存User成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "保存失败");
    }

    @ApiOperation(value = "更新User", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @PutMapping(value = "/user", produces = "application/json;charset=UTF-8")
    public BaseResult<?> updateUser(
            @RequestParam(value = "id", required = true, defaultValue = "") String id,
            @ApiParam @RequestBody UserFormDTO userFormDTO) {
        try {
            if (userService.get(id) == null) {
                return getBaseResultFail(false, "操作失败，不存在的User。请传入有效的User的ID。");
            }
            User user = new User();
            user.setId(id);
            user.setHeadImg(userFormDTO.getHeadImg());
            user.setOpenId(userFormDTO.getOpenId());
            user.setNickName(userFormDTO.getNickName());
            user.setUserName(userFormDTO.getUserName());
            userService.save(user);
            return getBaseResultSuccess(true, "保存User成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "保存失败");
    }

    @ApiOperation(value = "User列表查询", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/user", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listUser(HttpServletRequest request, HttpServletResponse response, @ApiParam UserQueryDTO userQueryDTO) {
        try {
            User user = new User();
            user.setHeadImg(userQueryDTO.getHeadImg());
            user.setOpenId(userQueryDTO.getOpenId());
            user.setNickName(userQueryDTO.getNickName());
            user.setUserName(userQueryDTO.getUserName());
            Page<User> page = userService.findPage(new Page<User>(request, response), user);
            if (page.getList().isEmpty()) {
                return getBaseResultSuccess(new ArrayList<User>(), "没有查询到有效的数据。");
            }
            return getBaseResultSuccess(page, "查询数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(null, "查询数据失败");
    }

    /**
     * 登录
     */
    public BaseResult<?> login(HttpServletRequest request, String code, @RequestBody UserFormDTO userFormDTO) throws IOException {
        //code -> session-key open_id
        if (StringUtils.isBlank(code)) {
            return getBaseResultSuccess(null, ResponseConstant.INVALID_PARAM.getCode(), ResponseConstant.INVALID_PARAM.getMsg());
        }

        JSONObject ret = JSON.parseObject(HttpClientUtils.INSTANCE.sendGet(loginUrl));
        if (ret.containsKey("errcode")) {
            return getBaseResultFail(null, "获取openId失败");
        }

        String session_key = (String) ret.get("session_key");
        String openid = (String) ret.get("openid");
        //保存用户信息
        userService.save(userFormDTO);

        //返回accessToken
        String accessToken = LoginTokenHelper.setSession(session_key, openid);

        return getBaseResultSuccess(accessToken, ResponseConstant.OK.getCode(), ResponseConstant.OK.getMsg());
    }

}
