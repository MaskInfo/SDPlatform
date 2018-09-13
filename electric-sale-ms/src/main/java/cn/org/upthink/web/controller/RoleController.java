package cn.org.upthink.web.controller;

import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.dto.RoleFormDTO;
import cn.org.upthink.model.dto.RoleQueryDTO;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.web.BaseController;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.entity.Role;
import cn.org.upthink.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* Created by rover on 2018-06-08.
*/
@RestController
@RequestMapping(value = "/v1")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/role/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> findRole(@PathVariable("id") String id) {
        Role role = null;
        try {
            role = roleService.get(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(role!=null){
            return getBaseResultSuccess(role, "有效对象");
        }else{
            return getBaseResultFail(null, "无效的id，没有获取到对象");
        }
    }

    @DeleteMapping(value = "/role/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> deleteRole(@PathVariable("id") String id) {
        Role role = null;
        try {
            role = roleService.get(id);
            if(role!=null){
                roleService.delete(role);
                return getBaseResultSuccess(true, "已成功删除Role对象");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "无效的id，没有删除Role对象");
    }

    @PostMapping(value = "/role", produces = "application/json;charset=UTF-8")
    public BaseResult<?> addRole(@ApiParam @RequestBody RoleFormDTO roleFormDTO) {
        try {
            Role role = new Role();
            role.setRoleName(roleFormDTO.getRoleName());
            role.setRoleType(roleFormDTO.getRoleType());
            roleService.save(role);
            return getBaseResultSuccess(true, "保存Role成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "保存失败");
    }

    @PutMapping(value = "/role", produces = "application/json;charset=UTF-8")
    public BaseResult<?> updateRole(
                     @RequestParam(value = "id", required = true, defaultValue = "") String id,
                     @ApiParam @RequestBody RoleFormDTO roleFormDTO) {
        try {
            if(roleService.get(id)==null){
                return getBaseResultFail(false, "操作失败，不存在的Role。请传入有效的Role的ID。");
            }
            Role role = new Role();
            role.setId(id);
            role.setRoleName(roleFormDTO.getRoleName());
            role.setRoleType(roleFormDTO.getRoleType());
            roleService.save(role);
            return getBaseResultSuccess(true, "保存Role成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "保存失败");
    }

    @GetMapping(value = "/role", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listRole(HttpServletRequest request, HttpServletResponse response, @ApiParam RoleQueryDTO roleQueryDTO) {
        try {
            Role role = new Role();
            role.setRoleName(roleQueryDTO.getRoleName());
            role.setRoleType(roleQueryDTO.getRoleType());
            Page<Role> page = roleService.findPage(new Page<Role>(request, response), role);
            if(page.getList().isEmpty()){
                return getBaseResultSuccess(new ArrayList<Role>(), "没有查询到有效的数据。");
            }
            return getBaseResultSuccess(page, "查询数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(null, "查询数据失败");
    }
    @GetMapping(value = "/role/user", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listRoleUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            UserFormDTO userInfo = LoginTokenHelper.INSTANCE.getUserInfo(stringRedisTemplate, request);
            if(userInfo == null){
                return getBaseResultSuccess(null, "请登录。");
            }
            Role role = new Role();
            role.setUserId(userInfo.getUserId());
            Page<Role> page = roleService.findPage(new Page<Role>(request, response), role);
            if(page.getList().isEmpty()){
                return getBaseResultSuccess(new ArrayList<Role>(), "没有查询到有效的数据。");
            }
            return getBaseResultSuccess(page, "查询数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(null, "查询数据失败");
    }

}
