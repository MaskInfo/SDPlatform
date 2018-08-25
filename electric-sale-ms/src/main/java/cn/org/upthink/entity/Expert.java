package cn.org.upthink.entity;

import cn.org.upthink.gen.annotation.TableField;
import cn.org.upthink.model.type.ExpertStateEnum;
import cn.org.upthink.persistence.mybatis.entity.BaseDataEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Copyright (C), 2018-2018
 * FileName: Expert
 * Author: Connie
 * Date: 2018/8/24 15:32
 * Description:
 */
@Getter
@Setter
@ToString
public class Expert extends BaseDataEntity<Expert> {
    @TableField(name = "expert_name",isQuery = true,required = true,remark = "名称")
    private String expertName;
    @TableField(name = "expert_resume",isQuery = true,required = true,remark = "简介")
    private String expertResume;
    @TableField(name = "expert_detail",isQuery = true,required = true,remark = "详情")
    private String expertDetail;
    @TableField(name = "telephone",isQuery = true,required = true,remark = "电话")
    private String telephone;
    @TableField(name = "email",isQuery = true,required = true,remark = "邮箱")
    private String email;
    @TableField(name = "state",isQuery = true,required = true,remark = "状态")
    private Integer state = ExpertStateEnum.WAIT_AUDIT.getStateCode();
    @TableField(name = "quiz_price",isQuery = true,required = true,remark = "提问价格")
    private String quizPrice;
    @TableField(name = "auditorId",isQuery = true,required = true,remark = "审核人ID")
    private String auditorId;
    @TableField(name = "audit_date",isQuery = true,required = true,remark = "审核时间")
    private String auditDate;
    @TableField(name = "user_id",isQuery = true,required = true,remark = "关联用户id")
    private String userId;
}
