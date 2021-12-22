package com.jn.web.goods.dao;

import com.jn.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface SkuMapper extends Mapper<Sku> {

    Sku selectById(String id);

    /**
     * 扣减库存增加销量
     * @param skuId
     * @param num
     */
    @Update("update tb_sku set num = num - #{num}, sale_num = sale_num + #{num} where id=#{skuId} and num >= #{num}")
    public void decrCount(@Param(value = "skuId") String skuId, @Param(value = "num")Integer num);

    /**
     * 恢复库存, 恢复销量
     * @param skuId
     * @param num
     */
    @Update("update tb_sku set num = num + #{num}, sale_num = sale_num - #{num} where id=#{skuId}")
    public void incrCount(@Param(value = "skuId") String skuId, @Param(value = "num")Integer num);

}
