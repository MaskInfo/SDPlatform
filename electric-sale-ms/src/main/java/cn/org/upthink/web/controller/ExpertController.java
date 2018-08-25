package cn.org.upthink.web.controller;

import cn.org.upthink.model.dto.ExpertFormDTO;
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
@Api(value="expertApi", description = "专家Controller", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1/expert")
public class ExpertController extends BaseController {

    @Autowired
    private ExpertService expertService;

    @ApiOperation(value="专家申请", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public BaseResult<?> applyExpert(HttpServletRequest request, @ApiParam @RequestBody ExpertFormDTO expertFormDTO) {
        try {
            expertService.apply(expertFormDTO, request);
            return getBaseResultSuccess(true, "保存Expert成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "保存失败");
    }

    @ApiOperation(value = "专家列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public BaseResult<?> listExpert(HttpServletRequest request, HttpServletResponse response) {
        try {
            Page<Expert> page = expertService.findPage(new Page<Expert>(request, response), new Expert());
            if(page.getList().isEmpty()){
                return getBaseResultSuccess(new ArrayList<Expert>(), "没有查询到有效的数据。");
            }
            return getBaseResultSuccess(page, "查询数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(null, "查询数据失败");
    }

    @ApiOperation(value ="专家详细信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
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


    /*@ApiOperation(value = "更新Expert", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    }*/


}
