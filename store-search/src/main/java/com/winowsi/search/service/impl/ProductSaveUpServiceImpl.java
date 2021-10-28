package com.winowsi.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.winowsi.common.to.es.SkuEsModel;
import com.winowsi.search.config.StoreElasticsearchConfig;
import com.winowsi.search.constant.EsConstant;
import com.winowsi.search.service.ProductSaveUpService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/21 15:08
 */
@Service
@Slf4j
public class ProductSaveUpServiceImpl implements ProductSaveUpService {
    @Autowired
    private RestHighLevelClient client;
    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        //1.先在es中建立好映射关系

        //2.保存数据
        BulkRequest bulkRequest = new BulkRequest();
        skuEsModels.forEach(item->{
            //指定索引名字
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            //指定索引唯一标识id
            indexRequest.id(item.getSkuId().toString());
            //指定source内容
            indexRequest.source( JSON.toJSONString(item), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        BulkResponse bulk = client.bulk(bulkRequest, StoreElasticsearchConfig.COMMON_OPTIONS);
        //TODO 批量错误处理
        boolean b = bulk.hasFailures();

        log.info("商品上架完成",Arrays.stream(bulk.getItems()).map(item->item.getId()).collect(Collectors.toList()));
        return b;
    }
}
