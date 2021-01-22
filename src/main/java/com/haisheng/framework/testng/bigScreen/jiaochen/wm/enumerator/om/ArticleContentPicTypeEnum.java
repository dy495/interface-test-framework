package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.om;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 */
public enum ArticleContentPicTypeEnum {

    /**
     * 大图
     **/
    ONE_BIG("大图", 1),


    /**
     * 三小图
     **/
    THREE("三小图", 3),

    /**
     * 左一图
     **/
    ONE_LEFT("左一图", 1);

    private String desc;

    private int picNum;

    ArticleContentPicTypeEnum(String desc, int picNum) {
        this.desc = desc;
        this.picNum = picNum;
    }

    public String getDesc() {
        return desc;
    }

    public int getPicNum() {
        return picNum;
    }

    public static ArticleContentPicTypeEnum findByName(String name) {
        Optional<ArticleContentPicTypeEnum> any
                = Arrays.stream(values()).filter(s -> s.name().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "图片类型枚举不存在");
        return any.get();
    }
}
