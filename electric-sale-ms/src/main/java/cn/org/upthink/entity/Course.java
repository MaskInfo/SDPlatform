package cn.org.upthink.entity;

import cn.org.upthink.gen.annotation.TableField;
import cn.org.upthink.persistence.mybatis.entity.BaseDataEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter@Setter@ToString
public class Course extends BaseDataEntity<Course> {
    @TableField(name = "course_name",isQuery = true,required = true,remark = "课程名称")
    private String courseName;
    @TableField(name = "course_resume",isQuery = true,required = true,remark = "课程描述")
    private String courseResume;
    @TableField(name = "base_price",isQuery = true,required = true,remark = "基础价格")
    private BigDecimal basePrice;
    @TableField(name = "sale_price",isQuery = true,required = true,remark = "售价")
    private BigDecimal salcePrice;
    @TableField(name = "teach_id",isQuery = true,required = true,remark = "授课人")
    private String teachId;
    @TableField(name = "total_duration",isQuery = true,required = true,remark = "总时长")
    private Integer totalDuration;
    @TableField(name = "course_type",isQuery = true,required = true,remark = "课程类型")
    private Integer courseType;
}
