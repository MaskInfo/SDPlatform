package cn.org.upthink.service;

import cn.org.upthink.persistence.mybatis.dto.Page;
import cn.org.upthink.persistence.mybatis.service.BaseCrudService;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
//import cn.org.upthink.frame.modules.sys.utils.UserUtils;
import cn.org.upthink.mapper.ExpertMapper;
import cn.org.upthink.entity.Expert;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class ExpertService extends BaseCrudService<ExpertMapper, Expert> {

    @Transactional(readOnly = false)
    public Page<Expert> findPage(Page<Expert> page, Expert expert) {
    return super.findPage(page, expert);
    }

    @Transactional(readOnly = false)
    public void save(Expert expert) {
        // 如果没有审核权限，则将当前内容改为待审核状态
        /*if (!UserUtils.getSubject().isPermitted("school:expert:audit")){
            expert.setDelFlag(Expert.DEL_FLAG_AUDIT);
        }*/
        //expert.setUpdateBy(UserUtils.getUser());
        expert.setUpdateDate(new Date());
        if (StringUtils.isBlank(expert.getId())){
            expert.preInsert();
            dao.insert(expert);
        }else{
            expert.preUpdate();
            dao.update(expert);
        }
    }

    @Transactional(readOnly = false)
    public void delete(Expert expert) {
        super.delete(expert);
    }

}
