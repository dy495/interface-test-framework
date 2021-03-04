package com.haisheng.framework.testng.bigScreen.jiaochenonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.ImageUtil;

public class pcCreateGoodsOnline {
    public static DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp=new PublicParm();
    FileUtil file = new FileUtil();
    jiaoChenInfoOnline info = new jiaoChenInfoOnline();
    ScenarioUtilOnline jc = new ScenarioUtilOnline();
    public Long id;
    public String goods_name="商品"+System.currentTimeMillis();
    public String goods_description="商品描述"+System.currentTimeMillis();
    public Long first_category = info.first_category;
    public Long second_category = info.second_category;
    public Long third_category = info.third_category;
    public Long goods_brand = info.goods_brand;
    public JSONArray goods_pic_list=getPic();
    public String price = "99.98";
    public JSONArray select_specifications;
    public JSONArray goods_specifications_list;
    public String goods_detail="商品详情"+System.currentTimeMillis();
    public Boolean checkcode=true;

    public JSONArray getPic(){
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(info.filePath)).getString("pic_path");
        JSONArray picarr = new JSONArray();
        picarr.add(logo);
        picarr.add(logo);
        picarr.add(logo);
        picarr.add(logo);
        picarr.add(logo);
        return picarr;
    }
    public JSONArray getPicone(){
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(info.filePath)).getString("pic_path");
        JSONArray picarr = new JSONArray();
        picarr.add(logo);
        return picarr;
    }
}
