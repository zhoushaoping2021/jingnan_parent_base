package com.jn.web.goods.service.impl;

import com.jn.entity.Constants;

import com.jn.pojo.OrderItem;
import com.jn.pojo.Sku;
import com.jn.pojo.Spu;
import com.jn.util.IdWorker;
import com.jn.web.goods.service.CartService;
import com.jn.web.goods.service.SkuService;
import com.jn.web.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author muYan
 * @Version 1.0
 * @Since 2021-02-01
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SpuService spuService;

    @Autowired
    SkuService skuService;

    @Autowired
    IdWorker idWorker;

    @Override
    public Map<String, Object> list(String userName) {
        //1. 根据当前登录用户的用户名, 到redis中获取购物车集合数据返回
        List<OrderItem> orderItemList = redisTemplate.boundHashOps(Constants.REDIS_CART + userName).values();

        //2. 计算所有商品总价格, 总购买数量
        //总购买数量
        Integer totalCount = 0;
        //总购买商品的价格
        Integer totalPrice = 0;


        if (orderItemList != null) {
            for (OrderItem orderItem : orderItemList) {
                //计算总购买数量
                totalCount += orderItem.getNum();
                //计算总购买价格
                totalPrice += orderItem.getMoney();
            }
        }


        //2. 封装购物车列表页面需要的数据返回
        Map<String, Object> resultMap = new HashMap<>();
        //购物车中的购物项集合数据
        resultMap.put("orderItemList", orderItemList);
        //总购买数量
        resultMap.put("totalNum", totalCount);
        //总购买价格
        resultMap.put("totalPrice", totalPrice);

        return resultMap;
    }

    @Override
    public void addAndUpdate(String userName, String skuId, Integer num) {
        //1. 根据用户名到redis中获取购物车信息
        OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps(Constants.REDIS_CART + userName).get(skuId);
        //2. 判断redis中的购物车是否为空
        if (orderItem != null) {
            //3. 如果redis中的购物车不为空, 追加购物项
            //购买数量追加 = 原来购物项的购买数量 + 现在要购买的数量
            orderItem.setNum(orderItem.getNum() + num);
            //本方法支持购买数量增减调整, 判断最终的购买数量, 是否大于0件
            if (orderItem.getNum() <= 0) {
                //如果购买数量<=0件, 从购物车中删除这个购物项
                redisTemplate.boundHashOps(Constants.REDIS_CART + userName).delete(skuId);
                //返回
                return ;
            }
            //总价格 = 购买数量 * 单价
            orderItem.setMoney(orderItem.getNum() * orderItem.getPrice());
            //支付价格 = 购买数量 * 单价
            orderItem.setPayMoney(orderItem.getNum() * orderItem.getPrice());
        } else {
            //4. 如果redis中的购物车为空, 新建购物车, 然后将购物项放入其中
            orderItem = createOrderItem(skuId, num);
        }
        //5. 将最新的购物车, 从新放到redis中的, 覆盖redis中之前的购物车数据
        redisTemplate.boundHashOps(Constants.REDIS_CART + userName).put(skuId, orderItem);
    }

    @Override
    public void del(String userName, String skuId) {
        redisTemplate.boundHashOps(Constants.REDIS_CART + userName).delete(skuId);
    }

    @Override
    public void updateChecked(String userName, String skuId, Boolean checked) {
        //1. 获取购物项实体对象
        OrderItem orderItem = (OrderItem)redisTemplate.boundHashOps(Constants.REDIS_CART + userName).get(skuId);
        if (orderItem != null) {
            //2.更改购物项中的是否勾选属性值
            orderItem.setChecked(checked);
            //3. 将购物项从新放回到redis中
            redisTemplate.boundHashOps(Constants.REDIS_CART + userName).put(skuId, orderItem);
        }
    }

    /**
     * 创建购物项对象
     * @param skuId 库存id
     * @param num   购买数量
     * @return
     */
    private OrderItem createOrderItem(String skuId, Integer num) {
        //1. 判断传入的购买数量不能为空
        if (num <= 0) {
            throw new RuntimeException("购买数量最少是1件");
        }
        //2. 根据skuid, 获取sku对象
        Sku sku = skuService.findOneByskuId(skuId);
        if (sku == null) {
            throw new RuntimeException("skuId错误, 无效的skuId, 数据库中找不到对应的库存sku数据!");
        }

        //3. 根据spuId, 获取spu对象
        Spu spu = spuService.findById(sku.getSpuId());
        if (spu == null) {
            throw new RuntimeException("skuId错误, 无效的skuId, 数据库中找不到对应的spu商品数据!");
        }

        //4. 创建购物项对象(订单详情对象)
        OrderItem orderItem = new OrderItem();
        //订单详情(购物项)主键id
        orderItem.setId(String.valueOf(idWorker.nextId()));
        //购买数量
        orderItem.setNum(num);
        //商品id
        orderItem.setSpuId(spu.getId());
        //库存id
        orderItem.setSkuId(skuId);
        //单价
        orderItem.setPrice(sku.getPrice());
        //库存名称
        orderItem.setName(sku.getName());
        //总价钱
        orderItem.setMoney(orderItem.getNum() * orderItem.getPrice());
        //支付价钱
        orderItem.setPayMoney(orderItem.getNum() * orderItem.getPrice());
        //运费
        orderItem.setPostFee(0);
        //商品示例图片
        orderItem.setImage(sku.getImage());
        //一级分类
        orderItem.setCategoryId1(spu.getCategory1Id());
        //二级分类
        orderItem.setCategoryId2(spu.getCategory2Id());
        //三级分类
        orderItem.setCategoryId3(spu.getCategory3Id());
        //商品重量
        orderItem.setWeight(sku.getWeight());
        //购物车列表页面中, 复选框是否选中状态
        orderItem.setChecked(true);
        return orderItem;
    }
}
