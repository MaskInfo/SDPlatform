package cn.org.upthink.entity;


import cn.org.upthink.gen.annotation.TableField;
import cn.org.upthink.persistence.mybatis.entity.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties("handler")
public class User extends BaseDataEntity<User> {
    @TableField(name = "user_name",isQuery = true,required = true,remark = "用户名")
    private String userName;
    @TableField(name = "head_img",isQuery = true,required = true,remark = "头像地址")
    private String headImg;
    @TableField(name = "nick_name",isQuery = true,required = true,remark = "昵称")
    private String nickName;
    @TableField(name = "open_id",isQuery = true,required = true,remark = "openId")
    private String openId;

    private List<Role> roles;


}
