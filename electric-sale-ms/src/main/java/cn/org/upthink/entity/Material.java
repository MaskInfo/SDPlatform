package cn.org.upthink.entity;

import cn.org.upthink.gen.annotation.TableField;
import cn.org.upthink.persistence.mybatis.entity.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Getter@Setter
@JsonIgnoreProperties("handler")
public class Material extends BaseDataEntity<Material> {
    @TableField(name = "material_title",isQuery = true,required = true,remark = "资料名称")
    private String materialTitle;
    @TableField(name = "base_price",isQuery = true,required = true,remark = "基本价格")
    private BigDecimal basePrice;
    @TableField(name = "sale_price",isQuery = true,required = true,remark = "售价")
    private BigDecimal salePrice;
    @TableField(name = "size",isQuery = true,required = true,remark = "大小(M)")
    private Integer size;
    @TableField(name = "download_url",isQuery = true,required = true,remark = "下载地址")
    private String downloadUrl;
    @TableField(name = "img_url",isQuery = true,required = true,remark = "图片地址")
    private String imgUrl;
    private String userId;

    private boolean buyState;
}
