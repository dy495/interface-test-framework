package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.EdgePvAccuracy;
import com.haisheng.framework.model.bean.EdgePvRgn;
import com.haisheng.framework.model.bean.PVUV;
import com.haisheng.framework.model.bean.PVUVAccuracy;

import java.util.List;

/**
 * Created by yuhaisheng
 */

public interface IEdgePvDao {

    int insert(EdgePvRgn pv);

    List<EdgePvAccuracy> getAccuracyByDay(String day);
}
