package cn.org.upthink.model.dto;

import java.io.Serializable;

import cn.org.upthink.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@ApiModel(value="User对象", description="")
public class UserFormDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="用户Id", name="userId", required=true)
    private String userId;

    @ApiModelProperty(value="头像地址", name="headImg", required=true)
    private String headImg;

    @ApiModelProperty(value="openId", name="openId", required=true)
    private String openId;

    @ApiModelProperty(value="昵称", name="nickName", required=true)
    private String nickName;

    @ApiModelProperty(value="用户名", name="userName", required=true)
    private String userName;

    /**手动增加getter,setter*/

}