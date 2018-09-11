package cn.org.upthink.service;

import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.MaterialMapper;
import cn.org.upthink.entity.Material;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class MaterialService extends BaseCrudService<MaterialMapper, Material> {

    @Transactional(readOnly = false)
    public Page<Material> findPage(Page<Material> page, Material material) {
    return super.findPage(page, material);
    }

    @Transactional(readOnly = false)
    public void save(Material material) {
        // 如果没有审核权限，则将当前内容改为待审核状态
        /*if (!UserUtils.getSubject().isPermitted("school:material:audit")){
            material.setDelFlag(Material.DEL_FLAG_AUDIT);
        }*/
        //material.setUpdateBy(UserUtils.getUser());
        material.setUpdateDate(new Date());
        if (StringUtils.isBlank(material.getId())){
            material.preInsert();
            dao.insert(material);
        }else{
            material.preUpdate();
            dao.update(material);
        }
    }

    @Transactional(readOnly = false)
    public void delete(Material material) {
        super.delete(material);
    }

    public void bind(String id, String operationId) {
        this.dao.bind(id,operationId);
    }
}
