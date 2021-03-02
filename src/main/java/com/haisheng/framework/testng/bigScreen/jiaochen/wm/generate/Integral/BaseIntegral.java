package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Integral;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ExchangePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralExchangeStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.AbstractGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.ExchangePageScene;

import java.util.List;

/**
 * 积分兑换生成抽象类
 *
 * @author wangmin
 * @date 2021/1/20 14:38
 */
public abstract class BaseIntegral extends AbstractGenerator implements IIntegral {
    protected IntegralExchangeStatusEnum integralExchangeStatus;
    private final IScene integralScene;

    public BaseIntegral(BaseBuilder baseBuilder) {
        super(baseBuilder);
        this.integralExchangeStatus = baseBuilder.integralExchangeStatus;
        this.integralScene = baseBuilder.integralScene;
    }

    @Override
    public Long getIntegralId() {
        try {
            IntegralExchangeStatusEnum.findByDesc(integralExchangeStatus.getDesc());
            Preconditions.checkArgument(!isEmpty(), "visitor is null");
            logger("FIND " + integralExchangeStatus.name() + " START");
            Preconditions.checkArgument(counter(integralExchangeStatus) < 4, integralExchangeStatus.getDesc() + " 状态执行次数大于3次，强行停止，请检查此状态生成");
            List<ExchangePage> exchangePageLis = resultCollectToBean(ExchangePageScene.builder().build(), ExchangePage.class);
            ExchangePage exchangePage = exchangePageLis.stream().filter(e -> e.getStatusName().equals(integralExchangeStatus.getDesc())).findFirst().orElse(null);
            if (exchangePage != null) {
                logger("FIND " + integralExchangeStatus.name() + " FINISH");
                logger("IntegralId is: " + exchangePage.getId());
                return exchangePage.getId();
            }
            logger(integralExchangeStatus.name() + " DIDN'T FIND ");
            integralExchangeStatus.getIntegralBuilder().buildIntegralExchange().execute(visitor, integralScene);
            return getIntegralId();
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg.append(e);
        }
        return null;
    }

    @Override
    public abstract void execute(Visitor visitor, IScene scene);

    public static abstract class BaseBuilder extends AbstractBuilder<BaseBuilder> {
        private IntegralExchangeStatusEnum integralExchangeStatus;
        private IScene integralScene;

        /**
         * @param integralExchangeStatus 积分兑换状态
         * @return BaseBuilder.integralExchangeStatus
         */
        public BaseBuilder IntegralExchangeStatus(IntegralExchangeStatusEnum integralExchangeStatus) {
            this.integralExchangeStatus = integralExchangeStatus;
            return this;
        }

        /**
         * @param scene 构建初始产品的场景
         * @return BaseBuilder.createScene
         */
        public BaseBuilder createScene(IScene scene) {
            this.integralScene = scene;
            return this;
        }

        /**
         * 构建卡券
         *
         * @return 卡券生成器
         */
        public abstract IIntegral buildIntegralExchange();

        protected IIntegral buildProduct() {
            return buildIntegralExchange();
        }

    }

    /**
     * 递归计数器
     *
     * @param integralExchangeStatus 积分兑换状态
     * @return 执行此状态次数
     */
    private Integer counter(IntegralExchangeStatusEnum integralExchangeStatus) {
        logger("计数器次数：" + counter);
        if (this.integralExchangeStatus == integralExchangeStatus) {
            return counter += 1;
        }
        return counter;
    }
}
