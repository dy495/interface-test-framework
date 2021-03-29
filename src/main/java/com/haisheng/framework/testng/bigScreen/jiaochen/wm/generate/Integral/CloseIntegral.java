package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Integral;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralExchangeStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.ExchangeSwitchStatusScene;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * 关闭状态
 *
 * @author wangmin
 * @date 2021/1/20 16:41
 */
public class CloseIntegral extends AbstractIntegral {

    public CloseIntegral(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull VisitorProxy visitor, IScene scene) {
        logger("CREATE CLOSE START");
        Long integralId = new IntegralGenerator.Builder().IntegralExchangeStatus(IntegralExchangeStatusEnum.WORKING).buildIntegralExchange().getIntegralId();
        super.visitor = visitor;
        ExchangeSwitchStatusScene.builder().id(integralId).status(true).build().invoke(visitor, true);
        logger("CREATE CLOSE FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder {

        @Override
        public IIntegral buildIntegralExchange() {
            return new CloseIntegral(this);
        }
    }
}
