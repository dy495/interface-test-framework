package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.OnlinePVUV;
import com.haisheng.framework.model.bean.PVUVAccuracy;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liaoxiangru
 */

@Repository
public interface IOnlinePvUvDao {

    int insert(OnlinePVUV onlinePVUV);
}
