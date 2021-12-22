package com.jn.web.goods.service;

import com.jn.pojo.Category;
import org.springframework.web.bind.annotation.PathVariable;

public interface CategoryService {

    Category findOneById(Integer id);
}
