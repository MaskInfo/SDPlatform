package cn.org.upthink.mapper;

import cn.org.upthink.entity.Material;
import cn.org.upthink.persistence.mybatis.dao.CrudDao;
import cn.org.upthink.persistence.mybatis.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface MaterialMapper extends CrudDao<Material> {
    void bind( @Param("userId") String userId,@Param("materialId") String materialId);
}