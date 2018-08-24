package cn.org.upthink.model.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel(value="Role对象", description="")
public class RoleFormDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="角色名称", name="roleName", required=true)
    private String roleName;

    @ApiModelProperty(value="角色类型", name="roleType", required=true)
    private Integer roleType;

    /**手动增加getter,setter*/

}