package com.jn.web.search.repostory;

import com.jn.web.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<SkuInfo,Long> {
}
