package cn.org.upthink.model.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel(value="Expert对象", description="")
public class ExpertQueryDTO extends BaseQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="简介", name="expertResume", required=false)
    private String expertResume;

    @ApiModelProperty(value="名称", name="expertName", required=false)
    private String expertName;

    @ApiModelProperty(value="详情", name="expertDetail", required=false)
    private String expertDetail;

    @ApiModelProperty(value="电话", name="telephone", required=false)
    private String telephone;

    @ApiModelProperty(value="提问价格", name="quizPrice", required=false)
    private String quizPrice;

    @ApiModelProperty(value="审核人ID", name="auditorId", required=false)
    private String auditorId;

    @ApiModelProperty(value="状态", name="state", required=false)
    private Integer state;

    @ApiModelProperty(value="邮箱", name="email", required=false)
    private String email;

    @ApiModelProperty(value="审核时间", name="auditDate", required=false)
    private String auditDate;

    @ApiModelProperty(value="用户ID", name="userId", required=true)
    private String userId;

    /**手动增加getter,setter*/

}