package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Package;

import com.haisheng.framework.testng.bigScreen.itemPorsche.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.audit.AuditStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.PackageStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.AuditPackageStatusScene;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 拒绝状态
 *
 * @author wangmin
 * @date 2021/1/21 16:53
 */
public class RejectPackage extends AbstractPackage {

    public RejectPackage(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {
        logger("CREATE REJECT START");
        Long packageId = new PackageGenerator.Builder().visitor(visitor).status(PackageStatusEnum.AUDITING).buildPackage().getPackageId();
        super.visitor = visitor;
        AuditPackageStatusScene.builder().id(packageId).status(AuditStatusEnum.REFUSAL.name()).build().invoke(visitor);
        logger("CREATE REJECT FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public IPackage buildPackage() {
            return new RejectPackage(this);
        }
    }
}
