package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Package;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.PackageStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.CancelPackageScene;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * 撤销状态
 *
 * @author wangmin
 * @date 2021/1/20 16:41
 */
public class RecallPackage extends AbstractPackage {

    public RecallPackage(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull VisitorProxy visitor, IScene scene) {
        logger("CREATE RECALL START");
        Long packageId = new PackageGenerator.Builder().visitor(visitor).status(PackageStatusEnum.AUDITING).buildPackage().getPackageId();
        super.visitor = visitor;
        CancelPackageScene.builder().id(packageId).build().invoke(visitor, true);
        logger("CREATE RECALL FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public IPackage buildPackage() {
            return new RecallPackage(this);
        }
    }
}