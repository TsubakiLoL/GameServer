package org.tsubaki.DataBase.Mybatis.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.tsubaki.DataBase.Mybatis.Mapper.TableMapper;

import java.util.List;


@Service
public class TableService {

    @Autowired
    private TableMapper tableMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> selectTables(){
        return tableMapper.selectTable();
    }


    public void createAndTestTable() {
        // 创建表
        jdbcTemplate.execute("CREATE TABLE debug_test (" +
                "id INTEGER PRIMARY KEY, " +
                "value TEXT)");

        // 插入数据
        jdbcTemplate.update("INSERT INTO debug_test (id, value) VALUES (?, ?)", 1, "Test Value");

        // 查询验证
        String result = jdbcTemplate.queryForObject(
                "SELECT value FROM debug_test WHERE id = ?",
                String.class,
                1
        );

        System.out.println("查询结果: " + result);

    }

}
