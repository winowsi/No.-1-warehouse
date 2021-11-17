package com.winowsi.search.controller;

import com.winowsi.search.service.MallSearchService;
import com.winowsi.search.vo.SearchParam;
import com.winowsi.search.vo.SearchResult;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/11/2 13:48
 */
@Controller
public class SearchController {
    private final MallSearchService mallSearchService;

    public SearchController(MallSearchService mallSearchService) {
        this.mallSearchService = mallSearchService;
    }

    @GetMapping("/list.html")
    public String listIndex(SearchParam searchParam, Model model, HttpServletRequest request){
        searchParam.setQueryString(request.getQueryString());
        SearchResult result= mallSearchService.search(searchParam);
        model.addAttribute("result",result);
        return "list";
    }
}
