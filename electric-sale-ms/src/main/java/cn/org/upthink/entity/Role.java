package cn.org.upthink.entity;


import cn.org.upthink.gen.annotation.TableField;
import cn.org.upthink.persistence.mybatis.entity.BaseDataEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Role extends BaseDataEntity<Role> {

    @TableField(name = "role_name",isQuery = true,required = true,remark = "角色名称")
    private String roleName;
    @TableField(name = "role_type",isQuery = true,required = true,remark = "角色类型")
    private Integer roleType;

    private String userId;

}
