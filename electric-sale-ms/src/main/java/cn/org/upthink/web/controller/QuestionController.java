package cn.org.upthink.web.controller;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* Created by rover on 2018-06-08.
*/
@Api(value="questionApi", description = "问题Controller", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1/question")
public class QuestionController extends BaseController {

    @Autowired
    private QuestionService questionService;

    @ApiOperation(value = "问题列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(produces = "application/json;charset=UTF-8")
    public BaseResult<?> listQuestion(HttpServletRequest request, HttpServletResponse response, @ApiParam QuestionQueryDTO questionQueryDTO) {
        try {
            Page<Question> page = questionService.list(questionQueryDTO, request, response);
            if(page.getList().isEmpty()){
                return getBaseResultSuccess(new ArrayList<Question>(), "没有查询到有效的数据。");
            }
            return getBaseResultSuccess(page, "查询数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(null, "查询数据失败");
    }

    /*@ApiOperation(value ="获取question详细信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @GetMapping(value = "/question/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> findQuestion(@PathVariable("id") String id) {
        Question question = null;
        try {
            question = questionService.get(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(question!=null){
            return getBaseResultSuccess(question, "有效对象");
        }else{
            return getBaseResultFail(null, "无效的id，没有获取到对象");
        }
    }

    @ApiOperation(value = "删除Question信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/question/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> deleteQuestion(@PathVariable("id") String id) {
        Question question = null;
        try {
            question = questionService.get(id);
            if(question!=null){
                questionService.delete(question);
                return getBaseResultSuccess(true, "已成功删除Question对象");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "无效的id，没有删除Question对象");
    }

    @ApiOperation(value="新增Question", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/question", produces = "application/json;charset=UTF-8")
    public BaseResult<?> addQuestion(@ApiParam @RequestBody QuestionFormDTO questionFormDTO) {
        try {
            Question question = new Question();
            question.setQuesTitle(questionFormDTO.getQuesTitle());
            question.setQuesDetail(questionFormDTO.getQuesDetail());
            question.setQuesDate(questionFormDTO.getQuesDate());
            question.setQuestioner(questionFormDTO.getQuestioner());
            question.setAnswerer(questionFormDTO.getAnswerer());
            question.setAnsDate(questionFormDTO.getAnsDate());
            question.setAnsDetail(questionFormDTO.getAnsDetail());
            questionService.save(question);
            return getBaseResultSuccess(true, "保存Question成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "保存失败");
    }

    @ApiOperation(value = "更新Question", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @PutMapping(value = "/question", produces = "application/json;charset=UTF-8")
    public BaseResult<?> updateQuestion(
                     @RequestParam(value = "id", required = true, defaultValue = "") String id,
                     @ApiParam @RequestBody QuestionFormDTO questionFormDTO) {
        try {
            if(questionService.get(id)==null){
                return getBaseResultFail(false, "操作失败，不存在的Question。请传入有效的Question的ID。");
            }
            Question question = new Question();
            question.setId(id);
            question.setQuesTitle(questionFormDTO.getQuesTitle());
            question.setQuesDetail(questionFormDTO.getQuesDetail());
            question.setQuesDate(questionFormDTO.getQuesDate());
            question.setQuestioner(questionFormDTO.getQuestioner());
            question.setAnswerer(questionFormDTO.getAnswerer());
            question.setAnsDate(questionFormDTO.getAnsDate());
            question.setAnsDetail(questionFormDTO.getAnsDetail());
            questionService.save(question);
            return getBaseResultSuccess(true, "保存Question成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "保存失败");
    }*/

}
