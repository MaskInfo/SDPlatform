package cn.org.upthink.model.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel(value="CourseDirform对象", description="")
public class CourseDirFormDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="课程地址", name="url", required=true)
    private String url;

    @ApiModelProperty(value="课程标题", name="courseTitle", required=true)
    private String courseTitle;

    @ApiModelProperty(value="时长", name="duration", required=true)
    private Integer duration;

    @ApiModelProperty(value="排序号", name="sequence", required=true)
    private Integer sequence;

    @ApiModelProperty(value="观看人数", name="scanCount", required=true)
    private Integer scanCount;

    @ApiModelProperty(value="所属课程id", name="courseId", required=true)
    private String courseId;

    /**手动增加getter,setter*/

}