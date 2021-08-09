package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Package;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.generator.IGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.PackagePage;

public interface IPackage extends IGenerator {

    Long getPackageId();

    PackagePage getPackagePage();

}
