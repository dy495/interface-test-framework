package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Package;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.audit.AuditStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.PackageStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.AuditPackageStatusScene;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * 进行中状态
 *
 * @author wangmin
 * @date 2021/1/20 16:41
 */
public class WorkingPackage extends AbstractPackage {

    public WorkingPackage(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull VisitorProxy visitor, IScene scene) {
        logger("CREATE WORKING START");
        Long packageId = new PackageGenerator.Builder().visitor(visitor).status(PackageStatusEnum.AUDITING).buildPackage().getPackageId();
        super.visitor = visitor;
        AuditPackageStatusScene.builder().id(packageId).status(AuditStatusEnum.AGREE.name()).build().invoke(visitor);
        logger("CREATE WORKING FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public IPackage buildPackage() {
            return new WorkingPackage(this);
        }
    }
}
