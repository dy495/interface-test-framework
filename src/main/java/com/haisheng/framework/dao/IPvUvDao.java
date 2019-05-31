package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.PVUV;
import com.haisheng.framework.model.bean.PVUVAccuracy;

import java.util.List;

/**
 * Created by yuhaisheng
 */

public interface IPvUvDao {

    int insert(PVUV pvuv);

    List<PVUVAccuracy> getAccuracyByDay(String day);
}
