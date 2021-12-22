package com.jingnan.data.controller;

import com.jingnan.data.service.SkuDataService;
import com.jn.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequestMapping("/sku")
public class SkuDataController {

    @Autowired
    private SkuDataService dataService;

    @GetMapping("/create")
    public Result createData(int count) {
        return dataService.buildData(count);
    }

    @GetMapping("/batch")
    public Result createDataBatch(int count) {
        return dataService.buildDataBatch(count);
    }

    @GetMapping("/batch2")
    public Result createDataBatch2(int count) {
        return dataService.buildDataBatch2(count);
    }

    @GetMapping("/batch3")
    public Result createDataBatch3(int count) {
        return dataService.buildDataBatch3(count);
    }

    @GetMapping("/batch4")
    public Result createDataBatch4(int count) {
        return dataService.buildDataBatch4(count);
    }

}
