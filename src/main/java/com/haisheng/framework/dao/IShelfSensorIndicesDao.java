package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.IShelfSensorIndices;
import org.springframework.stereotype.Repository;

/**
 * Created by liaoxiangru
 */

@Repository
public interface IShelfSensorIndicesDao {

    int insert(IShelfSensorIndices sensorIndex);
}
