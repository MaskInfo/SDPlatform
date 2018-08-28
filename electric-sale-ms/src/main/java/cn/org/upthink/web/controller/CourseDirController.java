package cn.org.upthink.web.controller;

import cn.org.upthink.exception.BussinessException;
import cn.org.upthink.model.ResponseConstant;
import cn.org.upthink.model.dto.CourseDirFormDTO;
import cn.org.upthink.model.dto.CourseDirQueryDTO;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.web.BaseController;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.entity.CourseDir;
import cn.org.upthink.service.CourseDirService;

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
import java.util.List;
import java.util.Map;

/**
* Created by rover on 2018-06-08.
*/
@Api(value="courseDirApi", description = "courseDir的接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1")
public class CourseDirController extends BaseController {

    @Autowired
    private CourseDirService courseDirService;

    @ApiOperation(value ="获取courseDir详细信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @GetMapping(value = "/courseDir/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> findCourseDir(@PathVariable("id") String id) {
        CourseDir courseDir = courseDirService.get(id);
        return getBaseResultSuccess(courseDir, "有效对象");
    }

    @ApiOperation(value = "删除CourseDir信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/courseDir/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> deleteCourseDir(@PathVariable("id") String id) {
        CourseDir courseDir = courseDirService.get(id);
        if(courseDir ==null){
            throw  new BussinessException(ResponseConstant.INVALID_PARAM.getCode(),"待删除对象不存在");
        }
        courseDirService.delete(courseDir);
        return getBaseResultSuccess(true, "已成功删除CourseDir对象");
    }

    @ApiOperation(value="新增CourseDir", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/courseDir", produces = "application/json;charset=UTF-8")
    public BaseResult<?> addCourseDir(@ApiParam @RequestBody CourseDirFormDTO courseDirFormDTO) {
        CourseDir courseDir = new CourseDir();
        courseDir.setUrl(courseDirFormDTO.getUrl());
        courseDir.setCourseTitle(courseDirFormDTO.getCourseTitle());
        courseDir.setDuration(courseDirFormDTO.getDuration());
        courseDir.setSequence(courseDirFormDTO.getSequence());
        courseDir.setScanCount(courseDirFormDTO.getScanCount());
        courseDir.setCourseId(courseDirFormDTO.getCourseId());
        courseDirService.save(courseDir);
        return getBaseResultSuccess(true, "保存CourseDir成功");
    }

    @ApiOperation(value = "更新CourseDir", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @PutMapping(value = "/courseDir", produces = "application/json;charset=UTF-8")
    public BaseResult<?> updateCourseDir(
                     @RequestParam(value = "id", required = true, defaultValue = "") String id,
                     @ApiParam @RequestBody CourseDirFormDTO courseDirFormDTO) {
        if(courseDirService.get(id)==null){
            return getBaseResultFail(null, "操作失败，不存在的CourseDir。请传入有效的CourseDir的ID。");
        }
        CourseDir courseDir = new CourseDir();
        courseDir.setId(id);
        courseDir.setUrl(courseDirFormDTO.getUrl());
        courseDir.setCourseTitle(courseDirFormDTO.getCourseTitle());
        courseDir.setDuration(courseDirFormDTO.getDuration());
        courseDir.setSequence(courseDirFormDTO.getSequence());
        courseDir.setScanCount(courseDirFormDTO.getScanCount());
        courseDir.setCourseId(courseDirFormDTO.getCourseId());
        courseDirService.save(courseDir);
        return getBaseResultSuccess(null, "保存CourseDir成功");
    }

    @ApiOperation(value = "CourseDir列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/courseDir", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listCourseDir(HttpServletRequest request, HttpServletResponse response, @ApiParam CourseDirQueryDTO courseDirQueryDTO) {
        CourseDir courseDir = new CourseDir();
        courseDir.setUrl(courseDirQueryDTO.getUrl());
        courseDir.setCourseTitle(courseDirQueryDTO.getCourseTitle());
        courseDir.setDuration(courseDirQueryDTO.getDuration());
        courseDir.setSequence(courseDirQueryDTO.getSequence());
        courseDir.setScanCount(courseDirQueryDTO.getScanCount());
        courseDir.setCourseId(courseDirQueryDTO.getCourseId());
        Page<CourseDir> page = courseDirService.findPage(new Page<CourseDir>(request, response), courseDir);
        return getBaseResultSuccess(page, "查询数据成功");
    }

}
