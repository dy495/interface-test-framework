package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.OnlineReqNum;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuhaisheng
 */

@Repository
public interface IOnlineReqNumDao {

    int updateDiff(OnlineReqNum onlinePVUV);

    int selectData(String deviceId, String date, String hour);
    List<String> getDeviceIdList(String date);
}
