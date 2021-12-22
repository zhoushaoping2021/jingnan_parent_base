package com.jn.search.mapper;

import com.jn.pojo.Sku;
import com.jn.search.pojo.SkuBig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuBigMapper extends Mapper<SkuBig> {
    /**
     * 查询表中有多少条数据
     * @return
     */
    @Select("select count(*) from ${table}")
    public Long countAll(@Param("table") String table);

    @Select("select * from tb_sku_big3 where id > #{id} order by id limit #{size}")
    public List<SkuBig> getListByPage(@Param("id") long startId, @Param("size") int size);


}
