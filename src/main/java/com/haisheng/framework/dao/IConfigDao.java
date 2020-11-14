package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.model.bean.Config;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by yuhaisheng
 */
@Repository
public interface IConfigDao {
    List<Config> queryOnlineConfigSummary();
    List<Config> queryDailyConfigSummary();
}