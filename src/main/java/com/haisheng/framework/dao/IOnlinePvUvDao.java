package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.OnlinePVUV;
import com.haisheng.framework.model.bean.PVUVAccuracy;

import java.util.List;

/**
 * Created by liaoxiangru
 */

public interface IOnlinePvUvDao {

    int insert(OnlinePVUV onlinePVUV);

    List<PVUVAccuracy> getAccuracyByDay(String day);
}
