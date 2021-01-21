package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer;

/**
 * @author ：Created by yanghang on 2020/6/18.  14:53
 */
public enum SexEnum {

    /**
     * 0 女  1 男
     **/
    FEMALE("女士", "女", 2),
    MALE("先生", "男", 1);


    private String desc;

    private String genderName;

    private int wechatGender;

    public String getGenderName() {
        return genderName;
    }

    public String getDesc() {
        return desc;
    }

    SexEnum(String desc, String genderName, int wechatGender) {
        this.desc = desc;
        this.genderName = genderName;
        this.wechatGender = wechatGender;
    }

    public static String findGenderByWechat(Integer wechatGender) {
        if (null == wechatGender) {
            return null;
        }
        if (FEMALE.wechatGender == wechatGender) {
            return FEMALE.name();
        }
        if (MALE.wechatGender == wechatGender) {
            return MALE.name();
        }
        return null;

    }

    public static SexEnum findByName(String name) {
        if (MALE.name().equals(name)) {
            return MALE;
        }
        if (FEMALE.name().equals(name)) {
            return FEMALE;
        }
        throw new IllegalArgumentException("性别不正确");
    }

    public static String getSex(Boolean isMale) {
        if (null == isMale) {
            return null;
        }
        if (isMale) {
            return SexEnum.MALE.getDesc();
        } else {
            return SexEnum.FEMALE.getDesc();
        }
    }

    public static SexEnum findById(int id) {
        if (FEMALE.ordinal() == id) {
            return FEMALE;
        } else if (MALE.ordinal() == id) {
            return MALE;
        }
        return null;
    }

    public static SexEnum findByGenderName(String genderName) {
        if (FEMALE.getGenderName().equals(genderName)) {
            return FEMALE;
        }
        if (MALE.getGenderName().equals(genderName)) {
            return MALE;
        }
        return null;
    }
}
