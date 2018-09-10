package cn.org.upthink.entity;

import cn.org.upthink.gen.annotation.TableField;
import cn.org.upthink.persistence.mybatis.entity.BaseDataEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter@Setter@ToString
public class CourseDir extends BaseDataEntity<CourseDir> {
    @TableField(name = "course_id",isQuery = true,required = true,remark = "所属课程id")
    private String courseId;
    @TableField(name = "course_title",isQuery = true,required = true,remark = "课程标题")
    private String courseTitle;
    @TableField(name = "sequence",isQuery = true,required = true,remark = "排序号")
    private Integer sequence;
    @TableField(name = "duration",isQuery = true,required = true,remark = "时长")
    private Integer duration;
    @TableField(name = "scanCount",isQuery = true,required = true,remark = "观看人数")
    private Integer scanCount;
    @TableField(name = "url",isQuery = true,required = true,remark = "课程地址")
    private String url;
}
