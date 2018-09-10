package cn.org.upthink.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel(value="Material对象", description="")
public class MaterialFormDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="售价", name="salePrice", required=true)
    private BigDecimal salePrice;

    @ApiModelProperty(value="下载地址", name="downloadUrl", required=true)
    private String downloadUrl;

    @ApiModelProperty(value="资料名称", name="materialTitle", required=true)
    private String materialTitle;

    @ApiModelProperty(value="图片地址", name="imgUrl", required=true)
    private String imgUrl;

    @ApiModelProperty(value="大小(M)", name="size", required=true)
    private Integer size;

    @ApiModelProperty(value="基本价格", name="basePrice", required=true)
    private BigDecimal basePrice;

    /**手动增加getter,setter*/

}