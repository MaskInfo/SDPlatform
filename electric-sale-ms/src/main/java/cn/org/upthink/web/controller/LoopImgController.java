package cn.org.upthink.web.controller;

import cn.org.upthink.anno.RequestLogging;
import cn.org.upthink.converter.Bean2VOConverter;
import cn.org.upthink.converter.DTO2BeanConverter;
import cn.org.upthink.model.dto.LoopImgFormDTO;
import cn.org.upthink.model.dto.LoopImgQueryDTO;
import cn.org.upthink.model.type.LoopTypeEnum;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
import cn.org.upthink.web.BaseController;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.entity.LoopImg;
import cn.org.upthink.service.LoopImgService;

import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.function.Supplier;

/**
* Created by rover on 2018-06-08.
*/
@Api(value="loopImgApi", description = "loopImg的接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1")
public class LoopImgController extends BaseController {

    @Autowired
    private LoopImgService loopImgService;

    @ApiOperation(value ="获取loopImg详细信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loopImgId", value = "loopImgId编号", required = true, dataType = "String")
    })
    @GetMapping(value = "/loopImg/{loopImgId}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> findLoopImg(@PathVariable("loopImgId") String loopImgId) {
        return tryCatch(()->{

            LoopImg loopImg = loopImgService.get(loopImgId);
            if(loopImg!=null){
                return getBaseResultSuccess(Bean2VOConverter.toLoopImgVo(loopImg), "有效对象");
            }else{
                return getBaseResultSuccess(null, "没有获取到对象");
            }

        });

    }

    @ApiOperation(value = "删除LoopImg信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loopImgId", value = "loopImgId编号", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/loopImg/{loopImgId}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> deleteLoopImg(@PathVariable("loopImgId") String loopImgId) {
        return tryCatch(()->{
            LoopImg loopImg = loopImgService.get(loopImgId);
            Preconditions.checkNotNull(loopImg,"待删除对象不存在");

            loopImgService.delete(loopImg);
            return getBaseResultSuccess(null, "已成功删除LoopImg对象");
        });
    }
    @RequestLogging
    @ApiOperation(value="新增LoopImg", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/loopImg", produces = "application/json;charset=UTF-8")
    public BaseResult<?> addLoopImg(@ApiParam @RequestBody LoopImgFormDTO loopImgFormDTO) {

        return tryCatch(()->{
            checkLoopImgFormDTO(loopImgFormDTO);
            final LoopImg loopImg = DTO2BeanConverter.getLoopImg(loopImgFormDTO);

            loopImgService.save(loopImg);


            return getBaseResultSuccess(Bean2VOConverter.toLoopImgVo(loopImg), "保存LoopImg成功");
        });
    }

    @ApiOperation(value = "更新LoopImg", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loopImgId", value = "loopImgId编号", required = true, dataType = "String")
    })
    @PutMapping(value = "/loopImg", produces = "application/json;charset=UTF-8")
    public BaseResult<?> updateLoopImg(
                     @RequestParam(value = "loopImgId", required = true, defaultValue = "") String loopImgId,
                     @ApiParam @RequestBody LoopImgFormDTO loopImgFormDTO) {
       return tryCatch(()->{
           Preconditions.checkState(StringUtils.isNotEmpty(loopImgId),"loopImgId 不能为空");
            checkLoopImgFormDTO(loopImgFormDTO);

           LoopImg loopImg = loopImgService.get(loopImgId);
            Preconditions.checkNotNull(loopImg,"操作失败，不存在的LoopImg。请传入有效的LoopImg的loopImgId。");

            BeanUtils.copyProperties(loopImgFormDTO,loopImg);

            loopImgService.save(loopImg);
            return getBaseResultSuccess(Bean2VOConverter.toLoopImgVo(loopImg), "保存LoopImg成功");
        });
    }

    @ApiOperation(value = "LoopImg列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/loopImg", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listLoopImg(HttpServletRequest request, HttpServletResponse response, @ApiParam LoopImgQueryDTO loopImgQueryDTO) {
        return tryCatch(()->{
            LoopImg loopImg = DTO2BeanConverter.getLoopImg(loopImgQueryDTO);

            Page<LoopImg> page = loopImgService.findPage(new Page<LoopImg>(request, response), loopImg);
            if(page.getList().isEmpty()){
                page.setList(Collections.EMPTY_LIST);
                return getBaseResultSuccess(page, "没有查询到有效的数据。");
            }
            return getBaseResultSuccess(page, "查询数据成功");
        });

    }

    private BaseResult<?> tryCatch(Supplier<BaseResult<?>> supplier){
        //try {
            return supplier.get();
        /*}catch (NullPointerException | IllegalArgumentException |IllegalStateException e){
            e.printStackTrace();
            logger.error("#####################" + e.getMessage()+"#######################");
            return getBaseResultFail(null, ResponseCode.INVALID_PARAM,e.getMessage());
        }catch(Exception e) {
            e.printStackTrace();
            logger.error("#####################"+e.getMessage()+"#######################");
            return getBaseResultFail(null,ResponseCode.HANDLER_EXCEPTION, "处理失败");
        }*/
    }

    private void checkLoopImgFormDTO(LoopImgFormDTO loopImgFormDTO){
        Preconditions.checkState(StringUtils.isNotEmpty(loopImgFormDTO.getLoopUrl()),"loopUrl 不能为空");
        Preconditions.checkState(StringUtils.isNotEmpty(loopImgFormDTO.getLoopName()),"loopName不能为空 不能为空");
        LoopTypeEnum.getSelf(loopImgFormDTO.getLoopType());

    }

}
