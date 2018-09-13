package cn.org.upthink.entity;

import cn.org.upthink.gen.annotation.TableField;
import cn.org.upthink.persistence.mybatis.entity.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Copyright (C), 2018-2018
 * FileName: Question
 * Author: Connie
 * Date: 2018/8/24 17:43
 * Description:
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties("handler")
public class Question extends BaseDataEntity<Question> {
    @TableField(name = "ques_title",isQuery = true,required = true,remark = "问题标题")
    private String quesTitle;
    @TableField(name = "ques_detail",isQuery = true,required = true,remark = "问题详情")
    private String quesDetail;
    @TableField(name = "ans_detail",isQuery = true,required = true,remark = "回答详情")
    private String ansDetail;
    @TableField(name = "questioner",isQuery = true,required = true,remark = "提问者")
    private User questioner;
    @TableField(name = "answerer",isQuery = true,required = true,remark = "回答者")
    private User answerer;
    @TableField(name = "question_date",isQuery = true,required = true,remark = "提问日期")
    private Date quesDate = new Date();
    @TableField(name = "answer_date",isQuery = true,required = true,remark = "回答日期")
    private Date ansDate;
    @TableField(name = "is_answer",isQuery = true,required = true,remark = "是否回答")
    private boolean isAnswer = false;
    @TableField(name = "is_pay",isQuery = true,required = true,remark = "是否支付")
    private boolean isPay = false;


}
