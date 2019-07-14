package com.haisheng.framework.dao;


import com.haisheng.framework.model.bean.Shelf;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IShelfDao {

    int insert(Shelf shelf);
    List<Shelf> query(String date);
}
