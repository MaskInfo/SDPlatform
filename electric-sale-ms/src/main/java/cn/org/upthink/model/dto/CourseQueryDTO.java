package cn.org.upthink.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel(value="Coursequery对象", description="")
public class CourseQueryDTO extends BaseQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="售价", name="salcePrice", required=false)
    private BigDecimal salcePrice;

    @ApiModelProperty(value="总时长", name="totalDuration", required=false)
    private Integer totalDuration;

    @ApiModelProperty(value="授课人", name="teachId", required=false)
    private String teachId;

    @ApiModelProperty(value="课程类型", name="courseType", required=false)
    private Integer courseType;

    @ApiModelProperty(value="课程名称", name="courseName", required=false)
    private String courseName;

    @ApiModelProperty(value="课程描述", name="courseResume", required=false)
    private String courseResume;

    @ApiModelProperty(value="基础价格", name="basePrice", required=false)
    private BigDecimal basePrice;

    @ApiModelProperty(value="用户id", name="userId", required=false)
    private String userId;

    /**手动增加getter,setter*/

}