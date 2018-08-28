package cn.org.upthink.converter;


import cn.org.upthink.entity.LoopImg;
import cn.org.upthink.model.type.LoopTypeEnum;
import cn.org.upthink.model.vo.LoopImgVO;
import org.springframework.beans.BeanUtils;

public class Bean2VOConverter {
    public static LoopImgVO toLoopImgVo(LoopImg bean){
        LoopImgVO vo = new LoopImgVO();
        BeanUtils.copyProperties(bean,vo,"loopType");
        vo.setLoopType(LoopTypeEnum.getSelf(bean.getLoopType()).name());
        return vo;
    }

}
