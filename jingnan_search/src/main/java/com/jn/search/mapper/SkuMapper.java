package com.jn.search.mapper;

import com.jn.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface SkuMapper extends Mapper<Sku> {
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
