package cn.org.upthink.web.controller;

import cn.org.upthink.model.dto.ExpertFormDTO;
import cn.org.upthink.model.dto.ExpertQueryDTO;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.web.BaseController;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.entity.Expert;
import cn.org.upthink.service.ExpertService;

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
@Api(value="expertApi", description = "expert的接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/redpacket")
public class ExpertController extends BaseController {

    @Autowired
    private ExpertService expertService;

    @ApiOperation(value ="获取expert详细信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @GetMapping(value = "/expert/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> findExpert(@PathVariable("id") String id) {
        Expert expert = null;
        try {
            expert = expertService.get(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(expert!=null){
            return getBaseResultSuccess(expert, "有效对象");
        }else{
            return getBaseResultFail(null, "无效的id，没有获取到对象");
        }
    }

    @ApiOperation(value = "删除Expert信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/expert/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> deleteExpert(@PathVariable("id") String id) {
        Expert expert = null;
        try {
            expert = expertService.get(id);
            if(expert!=null){
                expertService.delete(expert);
                return getBaseResultSuccess(true, "已成功删除Expert对象");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "无效的id，没有删除Expert对象");
    }

    @ApiOperation(value="新增Expert", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/expert", produces = "application/json;charset=UTF-8")
    public BaseResult<?> addExpert(@ApiParam @RequestBody ExpertFormDTO expertFormDTO) {
        try {
            Expert expert = new Expert();
            expert.setExpertResume(expertFormDTO.getExpertResume());
            expert.setExpertName(expertFormDTO.getExpertName());
            expert.setExpertDetail(expertFormDTO.getExpertDetail());
            expert.setTelephone(expertFormDTO.getTelephone());
            expert.setQuizPrice(expertFormDTO.getQuizPrice());
            expert.setAuditorId(expertFormDTO.getAuditorId());
            expert.setState(expertFormDTO.getState());
            expert.setEmail(expertFormDTO.getEmail());
            expert.setAuditDate(expertFormDTO.getAuditDate());
            expertService.save(expert);
            return getBaseResultSuccess(true, "保存Expert成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "保存失败");
    }

    @ApiOperation(value = "更新Expert", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @PutMapping(value = "/expert", produces = "application/json;charset=UTF-8")
    public BaseResult<?> updateExpert(
                     @RequestParam(value = "id", required = true, defaultValue = "") String id,
                     @ApiParam @RequestBody ExpertFormDTO expertFormDTO) {
        try {
            if(expertService.get(id)==null){
                return getBaseResultFail(false, "操作失败，不存在的Expert。请传入有效的Expert的ID。");
            }
            Expert expert = new Expert();
            expert.setId(id);
            expert.setExpertResume(expertFormDTO.getExpertResume());
            expert.setExpertName(expertFormDTO.getExpertName());
            expert.setExpertDetail(expertFormDTO.getExpertDetail());
            expert.setTelephone(expertFormDTO.getTelephone());
            expert.setQuizPrice(expertFormDTO.getQuizPrice());
            expert.setAuditorId(expertFormDTO.getAuditorId());
            expert.setState(expertFormDTO.getState());
            expert.setEmail(expertFormDTO.getEmail());
            expert.setAuditDate(expertFormDTO.getAuditDate());
            expertService.save(expert);
            return getBaseResultSuccess(true, "保存Expert成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "保存失败");
    }

    @ApiOperation(value = "Expert列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/expert", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listExpert(HttpServletRequest request, HttpServletResponse response, @ApiParam ExpertQueryDTO expertQueryDTO) {
        try {
            Expert expert = new Expert();
            expert.setExpertResume(expertQueryDTO.getExpertResume());
            expert.setExpertName(expertQueryDTO.getExpertName());
            expert.setExpertDetail(expertQueryDTO.getExpertDetail());
            expert.setTelephone(expertQueryDTO.getTelephone());
            expert.setQuizPrice(expertQueryDTO.getQuizPrice());
            expert.setAuditorId(expertQueryDTO.getAuditorId());
            expert.setState(expertQueryDTO.getState());
            expert.setEmail(expertQueryDTO.getEmail());
            expert.setAuditDate(expertQueryDTO.getAuditDate());
            Page<Expert> page = expertService.findPage(new Page<Expert>(request, response), expert);
            if(page.getList().isEmpty()){
                return getBaseResultSuccess(new ArrayList<Expert>(), "没有查询到有效的数据。");
            }
            return getBaseResultSuccess(page, "查询数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(null, "查询数据失败");
    }

}
