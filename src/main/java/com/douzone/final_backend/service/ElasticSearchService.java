package com.douzone.final_backend.service;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ElasticSearchService {
    @Bean
    RestHighLevelClient restHighLevelClient();

    void createIndex(String indexName) throws IOException;

    void createDocument(String index, String id, String jsonBody) throws IOException;

    GetResponse getDocument(String index, String id) throws IOException;

    List<Map<String, Object>> searchDocument(String index, String searchParam, String searchOption)
            throws Exception;
}
