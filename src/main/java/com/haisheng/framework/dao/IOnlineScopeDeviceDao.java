package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.OnlineScopeDevice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuhaisheng
 */

@Repository
public interface IOnlineScopeDeviceDao {

    List<OnlineScopeDevice> selectData(String day);
    int insert(OnlineScopeDevice onlinePVUV);
    int updateDiff(OnlineScopeDevice onlinePVUV);
}
