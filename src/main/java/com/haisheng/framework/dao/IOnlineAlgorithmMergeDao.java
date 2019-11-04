package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.OnlineAlgorithmMerge;
import com.haisheng.framework.model.bean.OnlineReqNum;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuhaisheng
 */

@Repository
public interface IOnlineAlgorithmMergeDao {

    List<OnlineAlgorithmMerge> selectDataByDate(String date);
}
