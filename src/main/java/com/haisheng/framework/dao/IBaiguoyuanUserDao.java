package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.BaiguoyuanBindUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuhaisheng
 */
@Repository
public interface IBaiguoyuanUserDao {

    List<BaiguoyuanBindUser> getUserList(String date);
    int removeData(String date, String shopId);

}
