package cn.org.upthink.model.dto;

import java.io.Serializable;
import java.util.Date;

import cn.org.upthink.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel(value="Questionquery对象", description="")
public class QuestionQueryDTO extends BaseQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="问题标题", name="quesTitle", required=false)
    private String quesTitle;

    @ApiModelProperty(value="问题详情", name="quesDetail", required=false)
    private String quesDetail;

    @ApiModelProperty(value="提问日期", name="quesDate", required=false)
    private Date quesDate;

    @ApiModelProperty(value="提问者", name="questioner", required=false)
    private User questioner;

    @ApiModelProperty(value="回答者", name="answerer", required=false)
    private User answerer;

    @ApiModelProperty(value="回答日期", name="ansDate", required=false)
    private Date ansDate;

    @ApiModelProperty(value="回答详情", name="ansDetail", required=false)
    private String ansDetail;

    private boolean answer;

    /**手动增加getter,setter*/

}