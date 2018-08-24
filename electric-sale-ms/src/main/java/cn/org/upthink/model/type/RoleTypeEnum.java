package cn.org.upthink.model.type;

import com.google.common.base.Preconditions;
import lombok.Getter;

@Getter
public enum RoleTypeEnum {
    NORMAL(0,"会员"),
    VIP(1,"VIP"),
    EXPERT(2,"专家");


    private final int type;
    private final String typeMsg;

    RoleTypeEnum(int type, String typeMsg) {
        this.type = type;
        this.typeMsg = typeMsg;
    }

    public static RoleTypeEnum getSelf(Integer roleType) {

        Preconditions.checkNotNull(roleType,"无效的roleType");
        for (RoleTypeEnum typeEnum : RoleTypeEnum.values()) {
            if(typeEnum.type == roleType){
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("无效的roleType");
    }

}
