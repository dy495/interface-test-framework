package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.OnlinePVUV;
import org.springframework.stereotype.Repository;

/**
 * Created by liaoxiangru
 */

@Repository
public interface IOnlinePvUvDao {

    int insert(OnlinePVUV onlinePVUV);
}
