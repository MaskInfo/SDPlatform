package cn.org.upthink.web.controller;

import cn.org.upthink.entity.Expert;
import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.dto.CourseFormDTO;
import cn.org.upthink.model.dto.CourseQueryDTO;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.util.QiniuMaterialUtil;
import cn.org.upthink.util.QiniuVideoUtil;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
* Created by rover on 2018-06-08.
*/
@Api(value="courseApi", description = "course的接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1")
public class CourseController extends BaseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @ApiOperation(value ="获取course详细信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })

    @GetMapping(value = "/course/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> findCourse(@PathVariable("id") String id,HttpServletRequest request) {
        Course course = courseService.get(id);
        //查询当前用户已购买课程
        UserFormDTO userInfo = LoginTokenHelper.INSTANCE.getUserInfo(stringRedisTemplate, request);
        if(userInfo != null){
            Course query = new Course();
            query.setUserId(userInfo.getUserId());
            query.setId(id);
            List<Course> cL = courseService.findList(query);
            if(!cL.isEmpty()){
                course.setBuyState(true);
            }
        }
        return getBaseResultSuccess(course, "有效对象");
    }

    @ApiOperation(value = "删除Course信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })

    @DeleteMapping(value = "/course/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> deleteCourse(@PathVariable("id") String id) {
        Course course = courseService.get(id);
        Preconditions.checkNotNull(course,String.format("不存在id为%s的对象",id));

        courseService.delete(course);
        return getBaseResultSuccess(true, "已成功删除Course对象");
    }

    @ApiOperation(value="上传课程视频", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })
    @PostMapping(value = "/course/upload", produces = "application/json;charset=UTF-8")
    public BaseResult<?> uploadCourse(HttpServletRequest request,@ApiParam("课程视频") MultipartFile file) {
        String url = QiniuVideoUtil.upLoadToQiNiu(request, file);

        return getBaseResultSuccess(url, "上传成功");
    }

    @ApiOperation(value="新增Course", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })
    @PostMapping(value = "/course", produces = "application/json;charset=UTF-8")
    public BaseResult<?> addCourse(@ApiParam @RequestBody CourseFormDTO courseFormDTO) {
        Course course = new Course();
        course.setSalcePrice(courseFormDTO.getSalcePrice());
        course.setTotalDuration(courseFormDTO.getTotalDuration());
        Expert user = new Expert();
        user.setId(courseFormDTO.getTeachId());
        course.setTeacher(user);
        course.setCourseType(courseFormDTO.getCourseType());
        course.setCourseName(courseFormDTO.getCourseName());
        course.setCourseResume(courseFormDTO.getCourseResume());
        course.setBasePrice(courseFormDTO.getBasePrice());
        course.setImgUrl(courseFormDTO.getImgUrl());
        course.setStartTime(courseFormDTO.getStartTime());
        courseService.save(course);
        return getBaseResultSuccess(course, "保存Course成功");
    }

    @ApiOperation(value = "更新Course", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
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
        Expert user = new Expert();
        user.setId(courseFormDTO.getTeachId());
        course.setTeacher(user);
        course.setCourseType(courseFormDTO.getCourseType());
        course.setCourseName(courseFormDTO.getCourseName());
        course.setCourseResume(courseFormDTO.getCourseResume());
        course.setBasePrice(courseFormDTO.getBasePrice());
        course.setImgUrl(courseFormDTO.getImgUrl());
        course.setStartTime(courseFormDTO.getStartTime());
        courseService.save(course);
        return getBaseResultSuccess(course, "保存Course成功");
    }

    @ApiOperation(value = "Course列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })
    @GetMapping(value = "/course", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listCourse(HttpServletRequest request, HttpServletResponse response, @ApiParam CourseQueryDTO courseQueryDTO) {
        Course course = new Course();
        course.setSalcePrice(courseQueryDTO.getSalcePrice());
        course.setTotalDuration(courseQueryDTO.getTotalDuration());
        Expert user = new Expert();
        user.setId(courseQueryDTO.getTeachId());
        course.setTeacher(user);
        course.setCourseType(courseQueryDTO.getCourseType());
        course.setCourseName(courseQueryDTO.getCourseName());
        course.setCourseResume(courseQueryDTO.getCourseResume());
        course.setBasePrice(courseQueryDTO.getBasePrice());
        Page<Course> page = courseService.findPage(new Page<Course>(request, response), course);

        Date now = new Date();
        //线下课程开课状态设置
        page.getList().stream().forEach(s->{
            if(s.getEndTime() != null && s.getEndTime().before(now)){
                s.setExpire(true);
                return;
            }
            s.setExpire(false);
        });
        //查询当前用户已购买课程
        UserFormDTO userInfo = LoginTokenHelper.INSTANCE.getUserInfo(stringRedisTemplate, request);
        if(userInfo != null){
            Course query = new Course();
            query.setUserId(userInfo.getUserId());
            Page<Course> buyPage = courseService.findPage(new Page<Course>(request, response), query);
            Set<String> idSet = buyPage.getList().stream().map(s -> s.getId()).collect(Collectors.toSet());
            //设置状态
            page.getList().forEach(s->{
                if(idSet.contains(s.getId())){
                    s.setBuyState(true);
                }else {
                    s.setBuyState(false);
                }
            });
        }

        return getBaseResultSuccess(page, "查询数据成功");
    }

    @ApiOperation(value = "已购买课程列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "string", paramType = "head")
    })
    @GetMapping(value = "/course/buy", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listCourseByUser(HttpServletRequest request, HttpServletResponse response,Integer type) {

        Course course = new Course();
        UserFormDTO userInfo = LoginTokenHelper.INSTANCE.getUserInfo(stringRedisTemplate, request);
        course.setUserId(userInfo.getUserId());
        course.setCourseType(type);
        Page<Course> page = courseService.findPage(new Page<Course>(request, response), course);

        return getBaseResultSuccess(page, "查询数据成功");
    }

}
