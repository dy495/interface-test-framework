package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Integral;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ExchangeGoodsDetail;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralExchangeStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.EditExchangeGoodsScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.ExchangeGoodsDetailedScene;
import com.haisheng.framework.util.DateTimeUtil;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * 过期状态
 *
 * @author wangmin
 * @date 2021/1/20 16:41
 */
public class ExpiredIntegral extends BaseIntegral {

    public ExpiredIntegral(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull Visitor visitor, IScene scene) {
        logger("CREATE EXPIRED START");
        String exchangeStartTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -2), "yyyy-MM-dd HH:mm:ss");
        String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -1), "yyyy-MM-dd HH:mm:ss");
        Long integralId = new IntegralGenerator.Builder().IntegralExchangeStatus(IntegralExchangeStatusEnum.WORKING).buildIntegralExchange().getIntegralId();
        super.visitor = visitor;
        ExchangeGoodsDetail exchangeGoodsDetail = JSONObject.toJavaObject(ExchangeGoodsDetailedScene.builder().id(integralId).build().execute(visitor, true), ExchangeGoodsDetail.class);
        EditExchangeGoodsScene.builder().exchangeGoodsType(exchangeGoodsDetail.getExchangeGoodsType()).goodsId(exchangeGoodsDetail.getGoodsId()).exchangePrice(exchangeGoodsDetail.getExchangePrice())
                .exchangeNum(exchangeGoodsDetail.getExchangeNum()).isLimit(exchangeGoodsDetail.getIsLimit()).exchangePeopleNum(exchangeGoodsDetail.getExchangePeopleNum())
                .exchangeStartTime(exchangeStartTime).exchangeEndTime(exchangeEndTime).id(integralId).build().execute(visitor, true);
        logger("CREATE EXPIRED FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder {

        @Override
        public IIntegral buildIntegralExchange() {
            return new ExpiredIntegral(this);
        }
    }
}
