package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Integral;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ExchangePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralExchangeStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.ExchangePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.ExchangeSwitchStatusScene;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 进行中状态
 *
 * @author wangmin
 * @date 2021/1/20 16:41
 */
public class WorkingIntegral extends BaseIntegral {

    public WorkingIntegral(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull Visitor visitor, IScene scene) {
        logger("CREATE WORKING START");
        List<ExchangePage> exchangePageLis = resultCollectToBean(ExchangePageScene.builder().build(), ExchangePage.class);
        ExchangePage exchangePage = exchangePageLis.stream().filter(e -> e.getStatusName().equals(IntegralExchangeStatusEnum.CLOSE.getDesc())).findFirst().orElse(null);
        if (exchangePage != null) {
            ExchangeSwitchStatusScene.builder().id(exchangePage.getId()).status(true).build().execute(visitor, true);
        } else {
            Preconditions.checkArgument(scene != null, "scene不能为空");
            scene.execute(visitor, true);
        }
        super.visitor = visitor;
        logger("CREATE WORKING FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder {

        @Override
        public IIntegral buildIntegralExchange() {
            return new WorkingIntegral(this);
        }
    }
}
