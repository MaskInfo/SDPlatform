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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
@Api(value = "userApi", description = "用户Controller", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/login/{code}", produces = "application/json;charset=UTF-8", method = RequestMethod.PATCH)
    public BaseResult<?> login(HttpServletRequest request, @PathVariable String code, @RequestBody UserFormDTO userFormDTO) throws Exception {
        if (StringUtils.isBlank(code)) {
            return getBaseResultSuccess(null, ResponseConstant.INVALID_PARAM.getCode(), ResponseConstant.INVALID_PARAM.getMsg());
        }

        String accessToken = userService.login(code, userFormDTO);
        if(StringUtils.isBlank(accessToken)){
            return getBaseResultSuccess(ResponseConstant.LOGIN_FAIL.getCode(), ResponseConstant.LOGIN_FAIL.getMsg());
        }

        return getBaseResultSuccess(accessToken, ResponseConstant.OK.getCode(), ResponseConstant.OK.getMsg());
    }

}
