package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.BaiguoyuanBindMetrics;
import com.haisheng.framework.model.bean.BaiguoyuanBindUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuhaisheng
 */
@Repository
public interface IBaiguoyuanMetricsDao {

    int insert(BaiguoyuanBindMetrics baiguoyuanBindMetrics);

}
