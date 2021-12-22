package com.jn.web.goods.controller;

import com.jn.entity.Result;
import com.jn.entity.StatusCode;
import com.jn.pojo.Goods;
import com.jn.pojo.Spu;
import com.jn.web.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
 * @Author yaxiongliu
 **/
@RestController
@RequestMapping("/spu")
public class SpuController {
    @Autowired
    private SpuService spuService;

    //新增商品
    @PostMapping("/addGoods")
    public Result addGoods(@RequestBody Goods goods){
        spuService.addGoods(goods);
        return new Result(true, StatusCode.OK, "新增商品成功");
    }
    //根据id查询
    @GetMapping("/goods/{spuId}")
    public Result findGoodsBySpuId(@PathVariable String spuId){
        Goods goods = spuService.findBySpuId(spuId);
        return new Result(true, StatusCode.OK, "查询成功", goods);
    }

    //修改商品
    @PutMapping("/updateGoods")
    public Result updateGoods(@RequestBody Goods goods){
        spuService.updateGoods(goods);
        return new Result(true, StatusCode.OK,"更新成功");
    }
    //审核商品
    @PutMapping("/auditGoods/{spuId}")
    public Result auditGoods(@PathVariable String spuId){
        spuService.auditGoods(spuId);
        return new Result(true, StatusCode.OK, "审核成功");
    }
    //上架商品
    @PutMapping("/upGoods/{spuId}")
    public Result upGoods(@PathVariable String spuId){
        spuService.upGoods(spuId);
        return new Result(true, StatusCode.OK, "上架成功");
    }

    //下架商品
    @PutMapping("/downGoods/{spuId}")
    public Result downGoods(@PathVariable String spuId){
        spuService.downGoods(spuId);
        return new Result(true, StatusCode.OK, "下架成功");
    }

    //逻辑删除商品
    @PutMapping("/deleteLogic/{spuId}")
    public Result deleteLogic(@PathVariable String spuId){
        spuService.deleteLogic(spuId);
        return new Result(true, StatusCode.OK, "逻辑删除成功");
    }

    //恢复商品
    @PutMapping("/restore/{spuId}")
    public Result restore(@PathVariable String spuId){
        spuService.restore(spuId);
        return new Result(true, StatusCode.OK, "恢复商品成功");
    }

    //商品物理删除
    @PutMapping("/deleteGoods/{spuId}")
    public Result deleteGoods(@PathVariable String spuId){
        spuService.deleteGoods(spuId);
        return new Result(true, StatusCode.OK, "物理删除成功");
    }
}
