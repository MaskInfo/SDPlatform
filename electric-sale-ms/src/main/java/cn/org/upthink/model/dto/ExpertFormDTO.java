package cn.org.upthink.model.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel(value="Expert对象", description="")
public class ExpertFormDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="简介", name="expertResume", required=true)
    private String expertResume;

    @ApiModelProperty(value="名称", name="expertName", required=true)
    private String expertName;

    @ApiModelProperty(value="详情", name="expertDetail", required=true)
    private String expertDetail;

    @ApiModelProperty(value="电话", name="telephone", required=true)
    private String telephone;

    @ApiModelProperty(value="提问价格", name="quizPrice", required=true)
    private String quizPrice;

    @ApiModelProperty(value="审核人ID", name="auditorId", required=true)
    private String auditorId;

    @ApiModelProperty(value="状态", name="state", required=true)
    private Integer state;

    @ApiModelProperty(value="邮箱", name="email", required=true)
    private String email;

    @ApiModelProperty(value="审核时间", name="auditDate", required=true)
    private String auditDate;

    @ApiModelProperty(value="用户ID", name="userId", required=true)
    private String userId;

    /**手动增加getter,setter*/

}