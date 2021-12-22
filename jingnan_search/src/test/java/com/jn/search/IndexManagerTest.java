package com.jn.search;

import com.jn.pojo.Sku;
import com.jn.search.pojo.SkuInfo;
import com.jn.search.service.SearchManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * @Author: sublun
 * @Date: 2021/2/3 11:08
 */
@SpringBootTest(classes = SearchApplication.class)
public class IndexManagerTest {
    @Autowired
    private SearchManagerService searchManagerService;
    @Autowired
    private ElasticsearchRestTemplate template;

    @Test
    public void createIndex() {

        template.putMapping(SkuInfo.class);
        //searchManagerService.createIndexAndMapping();
    }

    @Test
    public void deleteIndex() {
        searchManagerService.deleteIndex();
    }
    @Test
    public void importData() {
        //searchManagerService.importAllByPage(500);
        searchManagerService.importAll();
    }

}
