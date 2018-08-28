package cn.org.upthink.model.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel(value="Userquery对象", description="")
public class UserQueryDTO extends BaseQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="头像地址", name="headImg", required=false)
    private String headImg;

    @ApiModelProperty(value="openId", name="openId", required=false)
    private String openId;

    @ApiModelProperty(value="昵称", name="nickName", required=false)
    private String nickName;

    @ApiModelProperty(value="用户名", name="userName", required=false)
    private String userName;

    /**手动增加getter,setter*/

}