package com.haisheng.framework.testng.bigScreen.crm.wm.container;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public abstract class BaseContainer extends TestCaseCommon implements IContainer {
    protected static final Logger log = LoggerFactory.getLogger(BaseContainer.class);
    @Getter
    private String path;

    protected BaseContainer(BaseBuilder<?, ?> builder) {
        this.path = builder.path;
    }

    @Override
    public abstract boolean init();

    @Override
    public abstract List<Map<String, Object>> getTable();


    @Override
    public void setPath(String path) {
        if (!StringUtils.isEmpty(path)) {
            this.path = path;
        }
    }

    @Setter
    public static abstract class BaseBuilder<T extends BaseBuilder<?, ?>, R extends BaseContainer> {
        protected String path;

        public T path(String path) {
            this.path = path;
            return (T) this;
        }

        public abstract R buildContainer();

        public R build() {
            return buildContainer();
        }
    }
}
