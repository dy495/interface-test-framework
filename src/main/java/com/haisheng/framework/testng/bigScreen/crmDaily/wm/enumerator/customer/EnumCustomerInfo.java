package com.haisheng.framework.testng.bigScreen.crmDaily.wm.enumerator.customer;

import lombok.Getter;

/**
 * 客户信息枚举
 */
public enum EnumCustomerInfo {

    CUSTOMER_1("王先生", "MALE", "15555555555", "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。"),

    CUSTOMER_2("刘先生", "MALE", "17777777777",
            "汉皇重色思倾国，御宇多年求不得。\n" +
                    "杨家有女初长成，养在深闺人未识。\n" +
                    "天生丽质难自弃，一朝选在君王侧。\n" +
                    "回眸一笑百媚生，六宫粉黛无颜色。\n" +
                    "春寒赐浴华清池，温泉水滑洗凝脂。\n" +
                    "侍儿扶起娇无力，始是新承恩泽时。\n" +
                    "云鬓花颜金步摇，芙蓉帐暖度春宵。\n" +
                    "春宵苦短日高起，从此君王不早朝。\n" +
                    "承欢侍宴无闲暇，春从春游夜专夜。\n" +
                    "后宫佳丽三千人，三千宠爱在一身。\n" +
                    "金屋妆成娇侍夜，玉楼宴罢醉和春。\n" +
                    "姊妹弟兄皆列土，可怜光彩生门户。\n" +
                    "遂令天下父母心，不重生男重生女。\n" +
                    "骊宫高处入青云，仙乐风飘处处闻。\n" +
                    "缓歌慢舞凝丝竹，尽日君王看不足。\n" +
                    "渔阳鼙鼓动地来，惊破霓裳羽衣曲。\n" +
                    "九重城阙烟尘生，千乘万骑西南行。\n" +
                    "翠华摇摇行复止，西出都门百余里。\n" +
                    "六军不发无奈何，宛转蛾眉马前死。\n" +
                    "花钿委地无人收，翠翘金雀玉搔头。\n" +
                    "君王掩面救不得，回看血泪相和流。\n" +
                    "黄埃散漫风萧索，云栈萦纡登剑阁。\n" +
                    "峨嵋山下少人行，旌旗无光日色薄。\n" +
                    "蜀江水碧蜀山青，圣主朝朝暮暮情。\n" +
                    "行宫见月伤心色，夜雨闻铃肠断声。\n" +
                    "天旋地转回龙驭，到此踌躇不能去。\n" +
                    "马嵬坡下泥土中，不见玉颜空死处。\n" +
                    "君臣相顾尽沾衣，东望都门信马归。"),

    CUSTOMER_3("王先生", "MALE", "15321527989", "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔。"),

    CUSTOMER_4("李先生", "MALE", "18888888888", "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔。"),

    CUSTOMER_5("郭女士", "FEMALE", "", "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔。");

    EnumCustomerInfo(String name, String gender, String phone, String remark) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.remark = remark;
    }

    @Getter
    private final String name;
    @Getter
    private final String gender;
    @Getter
    private final String phone;
    @Getter
    private final String remark;
}
