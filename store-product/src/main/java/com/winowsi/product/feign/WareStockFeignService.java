package com.winowsi.product.feign;

import com.winowsi.common.utils.R;
import com.winowsi.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Tom
 */
@FeignClient("store-ware")
public interface WareStockFeignService {
    /**
     *
     * @param skuId s
     * @return
     */
    @PostMapping("ware/waresku/has/stock")
    R getSkuHasStock(@RequestBody List<Long> skuId);
}
