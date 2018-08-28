package cn.org.upthink.web.controller;

import cn.org.upthink.exception.BussinessException;
import cn.org.upthink.model.dto.CourseFormDTO;
import cn.org.upthink.model.dto.CourseQueryDTO;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.util.QiniuUtil;
import cn.org.upthink.web.BaseController;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.entity.Course;
import cn.org.upthink.service.CourseService;

import com.google.common.base.Preconditions;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* Created by rover on 2018-06-08.
*/
@Api(value="courseApi", description = "course的接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1")
public class CourseController extends BaseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation(value ="获取course详细信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @GetMapping(value = "/course/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> findCourse(@PathVariable("id") String id) {
        return getBaseResultSuccess(courseService.get(id), "有效对象");
    }

    @ApiOperation(value = "删除Course信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/course/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> deleteCourse(@PathVariable("id") String id) {
        Course course = courseService.get(id);
        Preconditions.checkNotNull(course,String.format("不存在id为%s的对象",id));

        courseService.delete(course);
        return getBaseResultSuccess(true, "已成功删除Course对象");
    }

    @ApiOperation(value="上传课程视频", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/course/upload", produces = "application/json;charset=UTF-8")
    public BaseResult<?> uploadCourse(HttpServletRequest request,@ApiParam("课程视频") MultipartFile file) {
        String url = QiniuUtil.upLoadToQiNiu(request, file);

        return getBaseResultSuccess(url, "上传成功");
    }



    @ApiOperation(value="新增Course", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/course", produces = "application/json;charset=UTF-8")
    public BaseResult<?> addCourse(@ApiParam @RequestBody CourseFormDTO courseFormDTO) {
        Course course = new Course();
        course.setSalcePrice(courseFormDTO.getSalcePrice());
        course.setTotalDuration(courseFormDTO.getTotalDuration());
        course.setTeachId(courseFormDTO.getTeachId());
        course.setCourseType(courseFormDTO.getCourseType());
        course.setCourseName(courseFormDTO.getCourseName());
        course.setCourseResume(courseFormDTO.getCourseResume());
        course.setBasePrice(courseFormDTO.getBasePrice());
        courseService.save(course);
        return getBaseResultSuccess(course, "保存Course成功");
    }

    @ApiOperation(value = "更新Course", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @PutMapping(value = "/course", produces = "application/json;charset=UTF-8")
    public BaseResult<?> updateCourse(
                     @RequestParam(value = "id", required = true, defaultValue = "") String id,
                     @ApiParam @RequestBody CourseFormDTO courseFormDTO) {
        if(courseService.get(id)==null){
            return getBaseResultFail(false, "操作失败，不存在的Course。请传入有效的Course的ID。");
        }
        Course course = new Course();
        course.setId(id);
        course.setSalcePrice(courseFormDTO.getSalcePrice());
        course.setTotalDuration(courseFormDTO.getTotalDuration());
        course.setTeachId(courseFormDTO.getTeachId());
        course.setCourseType(courseFormDTO.getCourseType());
        course.setCourseName(courseFormDTO.getCourseName());
        course.setCourseResume(courseFormDTO.getCourseResume());
        course.setBasePrice(courseFormDTO.getBasePrice());
        courseService.save(course);
        return getBaseResultSuccess(course, "保存Course成功");
    }

    @ApiOperation(value = "Course列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/course", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listCourse(HttpServletRequest request, HttpServletResponse response, @ApiParam CourseQueryDTO courseQueryDTO) {
        Course course = new Course();
        course.setSalcePrice(courseQueryDTO.getSalcePrice());
        course.setTotalDuration(courseQueryDTO.getTotalDuration());
        course.setTeachId(courseQueryDTO.getTeachId());
        course.setCourseType(courseQueryDTO.getCourseType());
        course.setCourseName(courseQueryDTO.getCourseName());
        course.setCourseResume(courseQueryDTO.getCourseResume());
        course.setBasePrice(courseQueryDTO.getBasePrice());
        Page<Course> page = courseService.findPage(new Page<Course>(request, response), course);

        return getBaseResultSuccess(page, "查询数据成功");
    }

}
