package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.Bug;
import org.springframework.stereotype.Repository;


/**
 * Created by yuhaisheng
 */
@Repository
public interface IBugDao {

    int insert(Bug bug);
}
