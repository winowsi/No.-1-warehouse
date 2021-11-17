package com.winowsi.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.winowsi.common.to.es.SkuEsModel;
import com.winowsi.common.utils.R;
import com.winowsi.search.AttrResponseVo;
import com.winowsi.search.config.StoreElasticsearchConfig;
import com.winowsi.search.constant.EsConstant;
import com.winowsi.search.feign.ProductFeignService;
import com.winowsi.search.service.MallSearchService;
import com.winowsi.search.vo.BrandVo;
import com.winowsi.search.vo.SearchParam;
import com.winowsi.search.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/8 9:50
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {
    private final RestHighLevelClient restHighLevelClient;
    private static final String UNDER_LINE = "_";
    private static final Integer NUMBER = 2;
    private static final String COLON = ":";
    private final ProductFeignService productFeignService;

    public MallSearchServiceImpl(RestHighLevelClient restHighLevelClient, ProductFeignService productFeignService) {
        this.restHighLevelClient = restHighLevelClient;
        this.productFeignService = productFeignService;
    }

    @Override
    public SearchResult search(SearchParam searchParam) {
        SearchResult searchResult = null;
        //1.动态构建出查询需要的dsl语句 准备检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);
        System.out.println(searchRequest);
        try {
            //执行检索请求
            SearchResponse response = restHighLevelClient.search(searchRequest, StoreElasticsearchConfig.COMMON_OPTIONS);
            //2.分析响应数据封装成我们需要的数据格式
            searchResult = buildSearchResult(response, searchParam);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    /**
     * 处理检索数据
     *
     * @param response 检索到的数据
     * @return 将需要的数据封装成对象返回
     */
    private SearchResult buildSearchResult(SearchResponse response, SearchParam searchParam) {
        SearchResult searchResult = new SearchResult();

        // 1.返回的所有查询到的商品品
        SearchHits hits = response.getHits();
        List<SkuEsModel> products = new ArrayList<>();
        if (null != hits.getHits() && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                SkuEsModel skuEsModel = JSON.parseObject(hit.getSourceAsString(), SkuEsModel.class);
                //高亮字段设置
                if (StringUtils.isNotEmpty(searchParam.getKeyword())) {
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String newSkuTitle = skuTitle.getFragments()[0].string();
                    skuEsModel.setSkuTitle(newSkuTitle);
                }
                products.add(skuEsModel);
            }
            searchResult.setProducts(products);
        }
        // 2.当前所有商品涉及到的所有属性信息
        ParsedNested attrAgg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        ArrayList<SearchResult.AttrVo> attrList = new ArrayList<>();
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            //attrId
            attrVo.setAttrId((Long) bucket.getKeyAsNumber());
            //attrName
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attr_name_agg");
            attrVo.setAttrName(attrNameAgg.getBuckets().get(0).getKeyAsString());
            //attrValue
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attr_value_agg");
            List<String> attrValues = attrValueAgg.getBuckets().stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
            attrVo.setAttrValue(attrValues);
            attrList.add(attrVo);
        }
        searchResult.setAttrs(attrList);

        // 3.当前所有商品涉及到的所有品牌信息
        ParsedLongTerms brandAgg = response.getAggregations().get("brand_agg");
        List<SearchResult.BrandVo> brands = new ArrayList<>();
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            //brandId
            brandVo.setBrandId(Long.valueOf(bucket.getKeyAsString()));
            //brandImg
            ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brand_img_agg");
            brandVo.setBrandImg(brandImgAgg.getBuckets().get(0).getKeyAsString());
            //brandName
            ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brand_name_agg");
            brandVo.setBrandName(brandNameAgg.getBuckets().get(0).getKeyAsString());
            brands.add(brandVo);
        }
        searchResult.setBrands(brands);

        // 4.当前所有商品涉及到的分类信息
        ParsedLongTerms catalogAgg = response.getAggregations().get("catalog_agg");
        List<SearchResult.CatalogVo> catalogs = new ArrayList<>();
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            //catalogId
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            catalogVo.setCatalogId(Long.valueOf(bucket.getKeyAsString()));
            //catalogName
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalog_name_agg");
            catalogVo.setCatalogName(catalogNameAgg.getBuckets().get(0).getKeyAsString());
            catalogs.add(catalogVo);
        }
        searchResult.setCatalogs(catalogs);
        // 5.分页信息-页码-总记录数-总页码
        searchResult.setPageNumber(searchParam.getPageNumber());
        long value = hits.getTotalHits().value;
        searchResult.setTotal(value);
        long privateAllPage = value % EsConstant.PAGE_SIZE == 0 ? value / EsConstant.PAGE_SIZE : (value / EsConstant.PAGE_SIZE + 1);
        searchResult.setTotalPages((int) privateAllPage);
        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 0; i <= privateAllPage; i++) {
            pageNavs.add(i);
        }
        searchResult.setPageNavs(pageNavs);
        //构建面包屑
        extracted(searchParam, searchResult);
        return searchResult;
    }

    /**
     * @param searchParam  检索参数
     * @param searchResult 检索条件
     */
    private void extracted(SearchParam searchParam, SearchResult searchResult) {
        if (null != searchParam.getAttrs() && searchParam.getAttrs().size() > 0) {
            List<SearchResult.NavVo> navVoList = searchParam.getAttrs().stream().map(item -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] split = item.split(UNDER_LINE);
                navVo.setNavValue(split[1]);
                //远程查询
                R r = productFeignService.info(Long.valueOf(split[0]));
                if (r.grtCode() == 0) {
                    AttrResponseVo attr = r.getData("attr", new TypeReference<AttrResponseVo>() {
                    });
                    navVo.setNavName(attr.getAttrName());
                }
                String replace = replayUrl(searchParam, item,"attrs");
                navVo.setLink("http://search.gulimall.com/list.html?" + replace);
                return navVo;
            }).collect(Collectors.toList());
            searchResult.setNavs(navVoList);
            if (null != searchParam.getBranId() && searchParam.getBranId().size() > 0) {
                List<SearchResult.NavVo> navs = searchResult.getNavs();
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                navVo.setNavName("品牌:");
                R info = productFeignService.info(searchParam.getBranId());
                if (info.grtCode() == 0) {
                    String replace="";
                    List<BrandVo> brand = info.getData("brand", new TypeReference<List<BrandVo>>() {});
                    StringBuilder stringBuffer=new StringBuilder();
                    for (BrandVo brandVo : brand) {
                        stringBuffer.append(brandVo.getBrandName()).append(";");
                        replace = replayUrl(searchParam, brandVo.getBrandId()+"","brandId");
                    }
                    navVo.setNavValue(stringBuffer.toString());
                    navVo.setLink("http://search.gulimall.com/list.html?" + replace);
                }
                navs.add(navVo);

            }
        }
    }

    /**
     * 编码转换
     * @param searchParam 检索参数对象
     * @param value 等于的值
     * @param key 替换的值
     * @return 拼接好的地址
     */
    private String replayUrl(SearchParam searchParam, String value,String key) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  searchParam.getQueryString().replace("&"+key+"" + encode, "");
    }

    /**
     * 准备检索请求
     * #模糊匹配,过滤,(按照属性,分类,品牌,价格区间,库存),排序,分页,高亮,聚合分析
     *
     * @return DSl执行语句
     */
    private SearchRequest buildSearchRequest(SearchParam searchParam) {

        //构建DSl
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /*
         * 模糊匹配,过滤,(按照属性,分类,品牌,价格区间,库存)
         */
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //模糊匹配
        if (StringUtils.isNotEmpty(searchParam.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword()));
        }
        //分类
        if (null != searchParam.getCatalog3Id()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalog3Id()));
        }
        //品牌
        if (null != searchParam.getBranId() && 0 < searchParam.getBranId().size()) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", searchParam.getBranId()));
        }
        //库存
        boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock() == 1));
        //价格区间
        if (StringUtils.isNotEmpty(searchParam.getSkuPrice())) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            String[] s = searchParam.getSkuPrice().split(UNDER_LINE);
            if (s.length == NUMBER) {
                rangeQueryBuilder.gte(s[0]).lte(s[1]);
            } else if (s.length == 1) {
                if (searchParam.getSkuPrice().startsWith(UNDER_LINE)) {
                    rangeQueryBuilder.lte(s[0]);
                }
                if (searchParam.getSkuPrice().endsWith(UNDER_LINE)) {
                    rangeQueryBuilder.gte(s[0]);
                }
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        //属性
        if (null != searchParam.getAttrs() && 0 < searchParam.getAttrs().size()) {

            for (String attr : searchParam.getAttrs()) {
                BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                String[] split = attr.split(UNDER_LINE);
                String atrId = split[0];
                String[] splits = split[1].split(COLON);
                nestedBoolQueryBuilder.must(QueryBuilders.termQuery("attrs.attrId", atrId));
                nestedBoolQueryBuilder.must(QueryBuilders.termsQuery("attrs.attrValue", splits));
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", nestedBoolQueryBuilder, ScoreMode.None);
                boolQueryBuilder.filter(nestedQueryBuilder);
            }

        }
        searchSourceBuilder.query(boolQueryBuilder);

        /*
         * 排序,分页,高亮
         */
        //排序
        if (StringUtils.isNotEmpty(searchParam.getSort())) {
            SortOrder sortOrder = "asc".equalsIgnoreCase(searchParam.getSort().split(UNDER_LINE)[1]) ? SortOrder.ASC : SortOrder.DESC;
            searchSourceBuilder.sort(searchParam.getSort().split(UNDER_LINE)[0], sortOrder);
        }
        //分页 from=(page-1)*size
        searchSourceBuilder.from((searchParam.getPageNumber() - 1) * EsConstant.PAGE_SIZE);
        searchSourceBuilder.size(EsConstant.PAGE_SIZE);
        //高亮
        if (StringUtils.isNotEmpty(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle").preTags("<b class='key' style='color:red'>").postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        /*
         * 聚合分析
         */
        //品牌聚合
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg").field("brandId").size(50);
        //品牌子聚合
        brandAgg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brandAgg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        searchSourceBuilder.aggregation(brandAgg);

        //三级分类聚合
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        //三级分类子聚合
        catalogAgg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        searchSourceBuilder.aggregation(catalogAgg);

        //属性聚合
        NestedAggregationBuilder nested = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        nested.subAggregation(attrIdAgg);
        searchSourceBuilder.aggregation(nested);

        return new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSourceBuilder);

    }
}
