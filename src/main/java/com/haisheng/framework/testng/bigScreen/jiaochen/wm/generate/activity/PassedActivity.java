package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.activity;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageApprovalScene;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 进行中的活动
 *
 * @author wangmin
 * @date 2021/1/27 13:58
 */
public class PassedActivity extends BaseActivity {
    protected PassedActivity(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(Visitor visitor, IScene scene) {
        logger("CREATE WORKING START");
        super.visitor = visitor;
        Long activityId = new ActivityGenerator.Builder().visitor(visitor).createScene(scene).activityStatusEnum(ActivityStatusEnum.PENDING).buildActivity().getActivityId();
        applyActivity(activityId);
        logger("CREATE WORKING FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseActivity.BaseBuilder {

        @Override
        public IActivity buildActivity() {
            return new PassedActivity(this);
        }
    }

    /**
     * 活动审批
     *
     * @param activityId 活动id
     */
    private void applyActivity(Long activityId) {
        List<Long> ids = new ArrayList<>();
        ids.add(activityId);
        IScene manageApprovalScene = ManageApprovalScene.builder().ids(ids).status(201).build();
        visitor.invokeApi(manageApprovalScene);
    }
}
