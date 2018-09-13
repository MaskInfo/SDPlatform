package cn.org.upthink.entity;


import cn.org.upthink.gen.annotation.TableField;
import cn.org.upthink.persistence.mybatis.entity.BaseDataEntity;
import cn.org.upthink.persistence.mybatis.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties("handler")
public class LoopImg extends BaseDataEntity<LoopImg> {
    @TableField(name = "loop_name",isQuery = true,required = true,remark = "轮播图名称")
    private String loopName;
    @TableField(name = "loop_url",isQuery = true,required = true,remark = "轮播图地址")
    private String loopUrl;
    @TableField(name = "loop_type",isQuery = true,required = true,remark = "轮播图类型(位置)")
    private Integer loopType;


}
