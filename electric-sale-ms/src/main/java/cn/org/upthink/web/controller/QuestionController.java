package cn.org.upthink.web.controller;

import cn.org.upthink.model.ResponseConstant;
import cn.org.upthink.model.dto.QuestionFormDTO;
import cn.org.upthink.model.dto.QuestionQueryDTO;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.web.BaseController;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.entity.Question;
import cn.org.upthink.service.QuestionService;

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
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rover on 2018-06-08.
 */
@Api(value = "questionApi", description = "问题Controller", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1/question")
public class QuestionController extends BaseController {

    @Autowired
    private QuestionService questionService;

    @ApiOperation(value = "问题列表查询", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public BaseResult<?> listQuestion(HttpServletRequest request, HttpServletResponse response, @ApiParam QuestionQueryDTO questionQueryDTO) {
        Page<Question> page = questionService.list(questionQueryDTO, request, response);
        if (page.getList().isEmpty()) {
            return getBaseResultSuccess(new ArrayList<Question>(), "没有查询到有效的数据。");
        }
        return getBaseResultSuccess(page, "查询数据成功");
    }

    @ApiOperation(value = "问题回答", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.PUT)
    public BaseResult<?> listQuestion(String questionId,String answerDetail) {
        Question question =new Question();
        question.setId(questionId);
        Question q = questionService.get(question);
        System.out.println(q);
        if(q == null){
            return getBaseResultFail(false, "操作失败，不存在的question。请传入有效的question的ID。");
        }
        if(!q.isPay() || q.isAnswer()){
            return getBaseResultFail(false, "操作失败，问题未支付 或 已回答");
        }
        question.setAnsDate(new Date());
        question.setAnswer(true);
        question.setAnsDetail(answerDetail);
        questionService.save(question);

        return getBaseResultSuccess(question, "回答成功");
    }

    @ApiOperation(value = "查询问题详情", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })
    @RequestMapping(produces = "application/json;charset=UTF-8",value = "/{id}",method = RequestMethod.PUT)
    public BaseResult<?> listQuestion(@PathVariable("id") String id) {
        Question question =new Question();
        question.setId(id);
        Question q = questionService.get(question);
        if(q == null){
            return getBaseResultFail(null, "操作失败，不存在的question。请传入有效的question的ID。");
        }
        return getBaseResultSuccess(q, "查询成功");
    }

}
