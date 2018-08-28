package cn.org.upthink.model.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel(value="Rolequery对象", description="")
public class RoleQueryDTO extends BaseQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="角色名称", name="roleName", required=false)
    private String roleName;

    @ApiModelProperty(value="角色类型", name="roleType", required=false)
    private Integer roleType;

    /**手动增加getter,setter*/

}