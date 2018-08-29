package cn.org.upthink.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Copyright (C), 2018-2018
 * FileName: PayDto
 * Author: Connie
 * Date: 2018/8/28 11:56
 * Description:
 */
@Getter
@Setter
@ApiModel(value="支付对象", description="")
public class PayFormDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="支付类型", name="payType", required=true)
    private String payType;

    @ApiModelProperty(value="支付金额", name="fee", required=true)
    private String fee;

    @ApiModelProperty(value="问题详情", name="quesDetail", notes = "支付类型为ASK，必填")
    private String quesDetail;

    @ApiModelProperty(value="问题标题", name="quesTitle", notes = "支付类型为ASK，必填")
    private String quesTitle;

    @ApiModelProperty(value="课程ID", name="quesTitle", notes = "支付类型为COURSE，必填")
    private String courseId;


}
