package com.haisheng.framework.model.experiment.checker;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.model.experiment.excep.DataExcept;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;

/**
 * 结果校验
 *
 * @author wangmin
 * @date
 */
public class ApiChecker implements IChecker {
    private final String errorMsg;
    private final String scenarios;
    private final TestCaseCommon testCaseCommon = new TestCaseCommon();

    protected ApiChecker(Builder builder) {
        this.errorMsg = builder.errorMessage;
        this.scenarios = builder.scenarios;
    }

    @Override
    public void check() {
        if (!StringUtils.isEmpty(errorMsg)) {
            try {
                throw new AssertionError(errorMsg);
            } catch (AssertionError e) {
                testCaseCommon.appendFailreason(errorMsg);
            } finally {
                testCaseCommon.saveData(scenarios);
            }
        }
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    public static class Builder {
        private String errorMessage;
        private String scenarios;

        /**
         * 验证的场景
         *
         * @param scenarios scenarios
         * @return Builder
         */
        public Builder scenario(String scenarios) {
            this.scenarios = scenarios;
            return this;
        }

        /**
         * 校验内容
         *
         * @param expression 表达式
         * @param message    错误信息
         * @return Builder
         */
        public Builder check(boolean expression, String message) {
            if (!expression) {
                this.errorMessage = message;
            }
            return this;
        }

        public ApiChecker build() {
            if (StringUtils.isBlank(scenarios)) {
                throw new DataExcept("必须输入验证场景");
            }
            return new ApiChecker(this);
        }
    }
}
