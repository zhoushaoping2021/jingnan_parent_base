package com.jn.web.search.controller;


import com.jn.entity.Result;
import com.jn.entity.StatusCode;

import com.jn.web.search.service.SearchManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/searchManage")
public class SearchManagerController {

    @Autowired
    private SearchManagerService searchManagerService;


    @PostMapping("/createIndexAndMapping")
    public Result createIndexAndMapping(){
        searchManagerService.createIndexAndMapping();
        return new Result(true, StatusCode.OK, "创建索引库成功");
    }

    @DeleteMapping("/deleteIndex")
    public Result deleteIndex(){
        searchManagerService.deleteIndex();
        return new Result(true, StatusCode.OK, "删除索引库成功");
    }


    @GetMapping("/importAll")
    public Result importAll(){
        searchManagerService.importAll();
        return new Result(true, StatusCode.OK, "导入全部数据成功");
    }

    @GetMapping("/importAll2")
    public Result importAll2(){
        return searchManagerService.importBigAll();
    }

    @GetMapping("/importBigAll")
    public Result importBigAll(){
        return searchManagerService.importBigAllByPage(1000);
    }

    @GetMapping("/importBigAll2")
    public Result importBigAll2(){
        return searchManagerService.importBigAllByPage2(1000);
    }

    @GetMapping("/importBigAll3")
    public Result importBigAll3(){
        return searchManagerService.importBigAllByPage3(1000);
    }
}
