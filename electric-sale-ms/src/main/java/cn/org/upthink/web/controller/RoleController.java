package cn.org.upthink.web.controller;

import cn.org.upthink.model.dto.RoleFormDTO;
import cn.org.upthink.model.dto.RoleQueryDTO;
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
@Api(value="roleApi", description = "role的接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/redpacket")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value ="获取role详细信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
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

    @ApiOperation(value = "删除Role信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
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

    @ApiOperation(value="新增Role", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @ApiOperation(value = "更新Role", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
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

    @ApiOperation(value = "Role列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

}
