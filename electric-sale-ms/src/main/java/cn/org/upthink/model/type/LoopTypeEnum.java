package cn.org.upthink.model.type;


import com.google.common.base.Preconditions;
import lombok.Getter;

@Getter
public enum LoopTypeEnum {
    HOME(0,"首页");


    private final int type;
    private final String typeMsg;

    LoopTypeEnum(int type, String typeMsg) {
        this.type = type;
        this.typeMsg = typeMsg;
    }

    public static LoopTypeEnum getSelf(Integer loopType) {

        Preconditions.checkNotNull(loopType,"无效的loopType");
        for (LoopTypeEnum typeEnum : LoopTypeEnum.values()) {
            if(typeEnum.type == loopType){
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("无效的loopType");
    }
}