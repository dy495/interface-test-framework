package com.haisheng.framework.dao;


import com.haisheng.framework.model.bean.ZentaoBug;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IZentaoBugDao {

    List<ZentaoBug> getAllBugs(String startDate);
}
