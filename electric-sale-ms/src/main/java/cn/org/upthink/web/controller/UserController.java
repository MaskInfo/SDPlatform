package cn.org.upthink.web.controller;

import cn.org.upthink.model.ResponseConstant;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
import cn.org.upthink.web.BaseController;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
