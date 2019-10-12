package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.OnlinePVUV;
import com.haisheng.framework.model.bean.OnlinePvuvCheck;
import org.springframework.stereotype.Repository;

/**
 * Created by yuhaisheng
 */

@Repository
public interface IOnlinePvUvDao {

    int insert(OnlinePVUV onlinePVUV);
    int updateDiff(OnlinePVUV onlinePVUV);

    OnlinePvuvCheck selectData(String com, String date, String hour);
}
