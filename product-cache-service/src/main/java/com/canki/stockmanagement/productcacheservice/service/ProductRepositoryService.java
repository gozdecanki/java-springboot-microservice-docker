package com.canki.stockmanagement.productcacheservice.service;

import com.canki.stockmanagement.productcacheservice.enums.Language;
import com.canki.stockmanagement.productcacheservice.repository.entity.Product;

public interface ProductRepositoryService {

    Product getProduct(Language language, Long productId);

    void deleteProducts(Language language);
}
