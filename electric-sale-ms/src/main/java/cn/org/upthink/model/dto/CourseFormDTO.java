package cn.org.upthink.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel(value="Courseform对象", description="")
public class CourseFormDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="售价", name="salcePrice", required=true)
    private BigDecimal salcePrice;

    @ApiModelProperty(value="总时长", name="totalDuration", required=true)
    private Integer totalDuration;

    @ApiModelProperty(value="授课人", name="teachId", required=true)
    private String teachId;

    @ApiModelProperty(value="课程类型", name="courseType", required=true)
    private Integer courseType;

    @ApiModelProperty(value="课程名称", name="courseName", required=true)
    private String courseName;

    @ApiModelProperty(value="课程描述", name="courseResume", required=true)
    private String courseResume;

    @ApiModelProperty(value="基础价格", name="basePrice", required=true)
    private BigDecimal basePrice;

    /**手动增加getter,setter*/

}