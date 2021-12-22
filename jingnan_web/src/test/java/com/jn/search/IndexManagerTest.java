package com.jn.search;


import com.jn.WebApplication;
import com.jn.web.search.service.SearchManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: sublun
 * @Date: 2021/2/3 11:08
 */
@SpringBootTest(classes = WebApplication.class)
public class IndexManagerTest {
    @Autowired
    private SearchManagerService searchManagerService;

    @Test
    public void createIndex() {
        searchManagerService.createIndexAndMapping();
    }

    @Test
    public void deleteIndex() {
        searchManagerService.deleteIndex();
    }
    @Test
    public void importData() {
        searchManagerService.importAllByPage(500);
//        searchManagerService.importAll();
    }

}
