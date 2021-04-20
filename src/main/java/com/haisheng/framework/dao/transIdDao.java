package com.haisheng.framework.dao;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface transIdDao {
    String SelectIdByNumber (@Param("trans_number")String trans_number);
    String SelectFaceUrlByTransId(@Param("trans_id")String trans_id);
}
