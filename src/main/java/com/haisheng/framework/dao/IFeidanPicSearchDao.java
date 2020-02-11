package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.FeidanPicSearch;
import com.haisheng.framework.model.bean.OnlineYuexiuCustomerSearch;
import org.springframework.stereotype.Repository;

/**
 * Created by yuhaisheng
 **/

@Repository
public interface IFeidanPicSearchDao {

    int insert(FeidanPicSearch feidanPicSearch);
}
