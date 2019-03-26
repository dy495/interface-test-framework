package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.Case;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by yuhaisheng
 */
@Repository
public interface ICaseDao {
    int insert(Case aCase);
    List<Integer> queryCaseByName(@Param("applicationId")int applicationId, @Param("configId")int configId, @Param("caseName")String caseName);
}