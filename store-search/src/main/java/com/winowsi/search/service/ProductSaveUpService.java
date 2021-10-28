package com.winowsi.search.service;

import com.winowsi.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author Tom
 */
public interface ProductSaveUpService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
