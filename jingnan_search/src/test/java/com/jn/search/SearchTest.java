package com.jn.search;

import com.jn.search.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: sublun
 * @Date: 2021/2/3 11:29
 */
@SpringBootTest(classes = SearchApplication.class)
public class SearchTest {
    @Autowired
    private SearchService searchService;

    @Test
    public void search() {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("keywords", "稻草人");
        Map<String, Object> result = searchService.search(paramMap);
        result.entrySet().forEach(e->{
            System.out.println(e.getKey());
            System.out.println("-----------------------------------");
            System.out.println(e.getValue());
        });
    }
}
