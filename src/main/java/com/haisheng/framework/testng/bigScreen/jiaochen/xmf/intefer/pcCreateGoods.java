package com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.jiaochen.jiaoChenInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;

public class pcCreateGoods {
    public static DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp=new PublicParm();
    FileUtil file = new FileUtil();
    jiaoChenInfo info = new jiaoChenInfo();
    public Long id;
    public String goods_name="商品"+System.currentTimeMillis();
    public String goods_description="商品描述"+System.currentTimeMillis();
    public Long first_category = info.first_category;
    public Long second_category = info.second_category;
    public Long third_category = info.third_category;
    public Long goods_brand = info.goods_brand;
    public JSONArray goods_pic_list=getPic();
    public String price;
    public JSONArray select_specifications;
    public JSONArray goods_specifications_list;
    public String goods_detail="商品详情"+System.currentTimeMillis();
    public Boolean checkcode=true;

    public JSONArray getPic(){
        String article_bg_pic=file.texFile(pp.filepath11);
        JSONArray list=new JSONArray();
        list.add(article_bg_pic);
        return list;
    }
}
