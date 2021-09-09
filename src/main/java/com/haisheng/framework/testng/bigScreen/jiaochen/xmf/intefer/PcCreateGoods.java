package com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.JiaoChenInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParam;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.ImageUtil;

public class PcCreateGoods {
    public static DateTimeUtil dt = new DateTimeUtil();
    PublicParam pp=new PublicParam();
    FileUtil file = new FileUtil();
    JiaoChenInfo info = new JiaoChenInfo();
    ScenarioUtil jc = new ScenarioUtil();
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
