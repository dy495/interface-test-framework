package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Integral;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
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
 * 未开始状态
 *
 * @author wangmin
 * @date 2021/1/20 16:41
 */
public class NoStartIntegral extends AbstractIntegral {

    public NoStartIntegral(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull VisitorProxy visitor, IScene scene) {
        logger("CREATE NO_START START");
        String exchangeStartTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), +2), "yyyy-MM-dd HH:mm:ss");
        String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), +3), "yyyy-MM-dd HH:mm:ss");
        Long integralId = new IntegralGenerator.Builder().IntegralExchangeStatus(IntegralExchangeStatusEnum.EXPIRED).buildIntegralExchange().getIntegralId();
        super.visitor = visitor;
        ExchangeGoodsDetail exchangeGoodsDetail = JSONObject.toJavaObject(ExchangeGoodsDetailedScene.builder().id(integralId).build().invoke(visitor, true), ExchangeGoodsDetail.class);
        EditExchangeGoodsScene.builder().exchangeGoodsType(exchangeGoodsDetail.getExchangeGoodsType()).goodsId(exchangeGoodsDetail.getGoodsId()).exchangePrice(exchangeGoodsDetail.getExchangePrice())
                .exchangeNum(exchangeGoodsDetail.getExchangeNum()).isLimit(exchangeGoodsDetail.getIsLimit()).exchangePeopleNum(exchangeGoodsDetail.getExchangePeopleNum())
                .exchangeStartTime(exchangeStartTime).exchangeEndTime(exchangeEndTime).id(integralId).build().invoke(visitor, true);
        logger("CREATE NO_START FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder {

        @Override
        public IIntegral buildIntegralExchange() {
            return new NoStartIntegral(this);
        }
    }
}
