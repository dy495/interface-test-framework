package com.haisheng.framework.dao;


import com.haisheng.framework.model.bean.Shelf;
import org.springframework.stereotype.Repository;


@Repository
public interface IShelfDao {

    int insert(Shelf shelf);
}
