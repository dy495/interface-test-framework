package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Package;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.audit.AuditStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.PackageStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.AuditPackageStatusScene;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * ζη»ηΆζ
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
        AuditPackageStatusScene.builder().id(packageId).status(AuditStatusEnum.REFUSAL.name()).build().visitor(visitor).execute();
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
