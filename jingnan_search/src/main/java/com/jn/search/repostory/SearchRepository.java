package com.jn.search.repostory;

import com.jn.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<SkuInfo,Long> {
}
