package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Package;

import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.IScene;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 卡券生成入口
 *
 * @author wangmin
 * @date 2021/1/20 14:54
 */
public class PackageGenerator extends AbstractPackage {

    public PackageGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {

    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public IPackage buildPackage() {
            return new PackageGenerator(this);
        }
    }
}
