package com.winowsi.product.feign;

import com.winowsi.common.to.es.SkuEsModel;
import com.winowsi.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Tom
 */
@FeignClient("store-search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
     R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
