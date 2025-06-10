package org.tsubaki.DataBase.Mybatis.Mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TableMapper {

    @Select("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name; ")
    public List<String> selectTable();
}
