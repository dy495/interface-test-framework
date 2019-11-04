package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.OnlineAlgorithmMerge;
import com.haisheng.framework.model.bean.OnlineScopeInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuhaisheng
 */

@Repository
public interface IOnlineScopeInfoDao {

    List<OnlineScopeInfo> selectData();
}
