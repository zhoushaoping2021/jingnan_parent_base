package com.jingnan.data.controller;

import com.jingnan.data.service.DataService;
import com.jn.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * @Author: sublun
 * @Date: 2021/6/25 22:28
 */
@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private DataService dataService;


    @GetMapping("clear-all")
    public Result clearAll() {
        dataService.clearAll();
        return Result.builder().flag(true).build();
    }

    @GetMapping("import-all")
    public Result importAll() throws SQLException {
        dataService.importAllData();
        return Result.builder().flag(true).build();
    }
}
