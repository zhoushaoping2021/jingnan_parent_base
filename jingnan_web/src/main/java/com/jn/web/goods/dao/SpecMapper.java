package com.jn.web.goods.dao;

import com.jn.pojo.Spec;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SpecMapper extends Mapper<Spec> {


    @Select("select * from tb_spec where template_id in (select template_id from tb_category where name = #{cateName})")
    List<Spec> findByCateName(@Param("cateName") String cateName);
}
