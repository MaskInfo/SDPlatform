package cn.org.upthink.web.controller;

import cn.org.upthink.model.dto.ExpertFormDTO;
import cn.org.upthink.model.type.ExpertStateEnum;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rover on 2018-06-08.
 */
@Api(value = "expertApi", description = "专家Controller", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1/expert")
public class ExpertController extends BaseController {

    @Autowired
    private ExpertService expertService;

    @ApiOperation(value = "专家申请", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public BaseResult<?> applyExpert(HttpServletRequest request, @ApiParam @RequestBody ExpertFormDTO expertFormDTO) {
        expertService.apply(expertFormDTO, request);
        return getBaseResultSuccess(true, "申请成功");
    }
    @ApiOperation(value = "专家审核", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/audit",produces = "application/json;charset=UTF-8", method = RequestMethod.PUT)
    public BaseResult<?> audit(HttpServletRequest request, @ApiParam @RequestParam("expertId") String expertId,@ApiParam @RequestParam("state") Integer state,@RequestParam("price") String price) {
        expertService.audit(expertId, state,price);
        return getBaseResultSuccess(true, "审核完成");
    }
    @ApiOperation(value = "专家列表查询", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public BaseResult<?> listExpert(HttpServletRequest request, HttpServletResponse response) {
        Expert expert = new Expert();
        expert.setState(ExpertStateEnum.AUDIT.getStateCode());
        Page<Expert> page = expertService.findPage(new Page<Expert>(request, response), expert);
        if (page.getList().isEmpty()) {
            return getBaseResultSuccess(new ArrayList<Expert>(), "没有查询到有效的数据。");
        }
        return getBaseResultSuccess(page, "查询数据成功");
    }

    @ApiOperation(value = "专家详细信息", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })
    @RequestMapping(value = "/{id}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public BaseResult<?> findExpert(@PathVariable("id") String id) {
        Expert expert = new Expert();
        expert.setId(id);
        expert.setState(null);
        expert = expertService.get(expert);
        if (expert != null) {
            return getBaseResultSuccess(expert, "有效对象");
        } else {
            return getBaseResultFail(null, "无效的id，没有获取到对象");
        }
    }

}
