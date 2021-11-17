package com.winowsi.search.service;

import com.winowsi.search.vo.SearchParam;
import com.winowsi.search.vo.SearchResult;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/8 9:50
 */

public interface MallSearchService {

    /**
     * 检索方法
     * @param searchParam 检索的所有参数
     * @return 返回检索到的结果,包含页面需要的所有信息
     */
    SearchResult search(SearchParam searchParam);
}
