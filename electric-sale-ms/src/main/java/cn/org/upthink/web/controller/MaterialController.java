package cn.org.upthink.web.controller;

import cn.org.upthink.entity.Course;
import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.dto.MaterialFormDTO;
import cn.org.upthink.model.dto.MaterialQueryDTO;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.web.BaseController;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.entity.Material;
import cn.org.upthink.service.MaterialService;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* Created by rover on 2018-06-08.
*/
@Api(value="materialApi", description = "material的接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/redpacket")
public class MaterialController extends BaseController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value ="获取material详细信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @GetMapping(value = "/material/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> findMaterial(@PathVariable("id") String id) {
        Material material = null;
        try {
            material = materialService.get(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(material!=null){
            return getBaseResultSuccess(material, "有效对象");
        }else{
            return getBaseResultFail(null, "无效的id，没有获取到对象");
        }
    }

    @ApiOperation(value = "删除Material信息", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/material/{id}", produces = "application/json;charset=UTF-8")
    public BaseResult<?> deleteMaterial(@PathVariable("id") String id) {
        Material material = null;
        try {
            material = materialService.get(id);
            if(material!=null){
                materialService.delete(material);
                return getBaseResultSuccess(true, "已成功删除Material对象");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(false, "无效的id，没有删除Material对象");
    }

    @ApiOperation(value="新增Material", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/material", produces = "application/json;charset=UTF-8")
    public BaseResult<?> addMaterial(@ApiParam @RequestBody MaterialFormDTO materialFormDTO) {
        try {
            Material material = new Material();
            material.setSalePrice(materialFormDTO.getSalePrice());
            material.setDownloadUrl(materialFormDTO.getDownloadUrl());
            material.setMaterialTitle(materialFormDTO.getMaterialTitle());
            material.setImgUrl(materialFormDTO.getImgUrl());
            material.setSize(materialFormDTO.getSize());
            material.setBasePrice(materialFormDTO.getBasePrice());
            materialService.save(material);
            return getBaseResultSuccess(material, "保存Material成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(null, "保存失败");
    }

    @ApiOperation(value = "更新Material", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id编号", required = true, dataType = "String")
    })
    @PutMapping(value = "/material", produces = "application/json;charset=UTF-8")
    public BaseResult<?> updateMaterial(
                     @RequestParam(value = "id", required = true, defaultValue = "") String id,
                     @ApiParam @RequestBody MaterialFormDTO materialFormDTO) {
        try {
            if(materialService.get(id)==null){
                return getBaseResultFail(false, "操作失败，不存在的Material。请传入有效的Material的ID。");
            }
            Material material = new Material();
            material.setId(id);
            material.setSalePrice(materialFormDTO.getSalePrice());
            material.setDownloadUrl(materialFormDTO.getDownloadUrl());
            material.setMaterialTitle(materialFormDTO.getMaterialTitle());
            material.setImgUrl(materialFormDTO.getImgUrl());
            material.setSize(materialFormDTO.getSize());
            material.setBasePrice(materialFormDTO.getBasePrice());
            materialService.save(material);
            return getBaseResultSuccess(material, "保存Material成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(null, "保存失败");
    }

    @ApiOperation(value = "Material列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/material", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listMaterial(HttpServletRequest request, HttpServletResponse response, @ApiParam MaterialQueryDTO materialQueryDTO) {
        try {
            Material material = new Material();
            material.setSalePrice(materialQueryDTO.getSalePrice());
            material.setDownloadUrl(materialQueryDTO.getDownloadUrl());
            material.setMaterialTitle(materialQueryDTO.getMaterialTitle());
            material.setImgUrl(materialQueryDTO.getImgUrl());
            material.setSize(materialQueryDTO.getSize());
            material.setBasePrice(materialQueryDTO.getBasePrice());
            Page<Material> page = materialService.findPage(new Page<Material>(request, response), material);
            if(page.getList().isEmpty()){
                return getBaseResultSuccess(new ArrayList<Material>(), "没有查询到有效的数据。");
            }

            page.getList().stream().forEach(s->{

            });

            return getBaseResultSuccess(page, "查询数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(null, "查询数据失败");
    }
    @ApiOperation(value = "Material购买列表查询", notes="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/material/buy", produces = "application/json;charset=UTF-8")
    public BaseResult<?> listMaterialBuy(HttpServletRequest request, HttpServletResponse response, @ApiParam MaterialQueryDTO materialQueryDTO) {
        try {
            Material material = new Material();
            material.setSalePrice(materialQueryDTO.getSalePrice());
            material.setDownloadUrl(materialQueryDTO.getDownloadUrl());
            material.setMaterialTitle(materialQueryDTO.getMaterialTitle());
            material.setImgUrl(materialQueryDTO.getImgUrl());
            material.setSize(materialQueryDTO.getSize());
            material.setBasePrice(materialQueryDTO.getBasePrice());
            //material.setUserId(userInfo.getUserId());
            Page<Material> page = materialService.findPage(new Page<Material>(request, response), material);
            if(page.getList().isEmpty()){
                return getBaseResultSuccess(new ArrayList<Material>(), "没有查询到有效的数据。");
            }
            //设置购买状态
            //查询当前用户已购买课程
            UserFormDTO userInfo = LoginTokenHelper.INSTANCE.getUserInfo(stringRedisTemplate, request);
            if(userInfo != null){
                Material query = new Material();
                query.setUserId(userInfo.getUserId());
                Page<Material> buyPage = materialService.findPage(new Page<Material>(request, response), query);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseResultFail(null, "查询数据失败");
    }

}
