package com.winowsi.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.product.entity.BrandEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 品牌
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:03:34
 */
public interface BrandService extends IService<BrandEntity> {


    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);

}

