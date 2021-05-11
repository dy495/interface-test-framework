package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Package;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.PackagePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.PackageStatusEnum;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.generator.BaseGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.PackageFormPageScene;

/**
 * 套餐生成抽象类
 *
 * @author wangmin
 * @date 2021/1/20 14:38
 */
public abstract class AbstractPackage extends BaseGenerator implements IPackage {
    protected PackageStatusEnum status;
    private final IScene scene;

    public AbstractPackage(AbstractBuilder abstractBuilder) {
        super(abstractBuilder);
        this.status = abstractBuilder.status;
        this.scene = abstractBuilder.scene;
    }

    @Override
    public Long getPackageId() {
        try {
            PackageStatusEnum.findById(status.getId());
            Preconditions.checkArgument(!isEmpty(), "visitor is null");
            logger("FIND " + status.name() + " START");
            Preconditions.checkArgument(counter(status) < 4, status.getName() + " 状态执行次数大于3次，强行停止，请检查此状态生成");
            PackagePage packagePage = getPage();
            if (packagePage != null) {
                logger("FIND " + status.name() + " FINISH");
                logger("packageId is: " + packagePage.getPackageId());
                logger("packageName is：" + packagePage.getPackageName());
                return packagePage.getPackageId();
            }
            status.getPackageBuilder().buildPackage().execute(visitor, scene);
            return getPackageId();
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg.append(e);
        }
        return null;
    }

    public static abstract class AbstractBuilder extends BaseBuilder<AbstractBuilder, IPackage> {
        private PackageStatusEnum status;
        private IScene scene;

        /**
         * @param status 套餐状态
         * @return BaseBuilder
         */
        public AbstractBuilder status(PackageStatusEnum status) {
            this.status = status;
            return this;
        }

        /**
         * @param scene 构建初始产品的场景
         * @return BaseBuilder
         */
        public AbstractBuilder scene(IScene scene) {
            this.scene = scene;
            return this;
        }

        /**
         * 构建套餐
         *
         * @return 套餐
         */
        public abstract IPackage buildPackage();

        @Override
        protected IPackage buildProduct() {
            return buildPackage();
        }
    }

    /**
     * 获取卡券表单
     *
     * @return 卡券表单
     */
    private PackagePage getPage() {
        IScene packageFormPageScene = PackageFormPageScene.builder().build();
        return findBeanByField(packageFormPageScene, PackagePage.class, "audit_status_name", status.getName());
    }

    /**
     * 递归计数器
     *
     * @param statusEnum 套餐 状态
     * @return 执行此状态次数
     */
    private Integer counter(PackageStatusEnum statusEnum) {
        logger("计数器次数：" + counter);
        return this.status == statusEnum ? counter += 1 : counter;
    }
}
