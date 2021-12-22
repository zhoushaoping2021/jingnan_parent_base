package com.jn.web.goods.service.impl;

import com.jn.pojo.Category;
import com.jn.web.goods.dao.CategoryMapper;
import com.jn.web.goods.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * @Author yaxiongliu
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category findOneById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }
}
