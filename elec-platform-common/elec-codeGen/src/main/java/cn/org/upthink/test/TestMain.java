package cn.org.upthink.test;

import cn.org.upthink.gen.JpaEntityFreeMarkerGenerator;

/**
 * run it !!!
 */
public class TestMain {
    //<--
    public static void main(String[] args){
        JpaEntityFreeMarkerGenerator fmGenerator = new JpaEntityFreeMarkerGenerator();
        fmGenerator.generate(WxActivityTime.class, "/Users/meishukai/gitProject/gen", true);
    }

}
