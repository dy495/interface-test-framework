package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.OnlineYuexiuCustomerSearch;
import com.haisheng.framework.model.bean.OnlineYuexiuUvGap;
import org.springframework.stereotype.Repository;

/**
 * Created by yuhaisheng
 **/

@Repository
public interface IOnlineYuexiuCustomerSearchDao {

    int insert(OnlineYuexiuCustomerSearch onlineYuexiu);
}
