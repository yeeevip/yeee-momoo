package com.learn.elasticsearch.demo.service;

import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 21:57
 */
public interface ElasticsearchService {

    /**
     * 创建索引
     */
    boolean createIndex(String indexName, String mapping) throws IOException;

    /**
     * 判断索引是否存在
     */
    boolean exists(String indexName) throws Exception;

    /**
     * 统计数量
     */
    long count(QueryBuilder queryBuilder, String... indices) throws Exception;

    /**
     * 删除索引
     */
    void delete(String indexName) throws Exception;

    /**
     * 批量保存
     */
    BulkResponse bulk(String index, List<Map<String, Object>> list) throws IOException;

}