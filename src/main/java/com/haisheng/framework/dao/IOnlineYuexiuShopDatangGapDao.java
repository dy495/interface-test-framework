package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.OnlinePVUV;
import com.haisheng.framework.model.bean.OnlinePvuvCheck;
import com.haisheng.framework.model.bean.OnlineYuexiuUvGap;
import org.springframework.stereotype.Repository;

/**
 * Created by yuhaisheng
 */

@Repository
public interface IOnlineYuexiuShopDatangGapDao {

    int insert(OnlineYuexiuUvGap onlineYuexiuUvGap);
}
