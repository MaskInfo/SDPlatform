package cn.org.upthink.gen;

import cn.org.upthink.entity.*;
import cn.org.upthink.test.WxActivityTime;

public class ElecGen {
    public static void main(String[] args) {
        JpaEntityFreeMarkerGenerator fmGenerator = new JpaEntityFreeMarkerGenerator();
        fmGenerator.generate(Material.class, "C:\\Users\\shukai\\Desktop\\gen", true);
    }
}
