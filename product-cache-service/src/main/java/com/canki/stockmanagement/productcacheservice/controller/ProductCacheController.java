package com.canki.stockmanagement.productcacheservice.controller;

import com.canki.stockmanagement.productcacheservice.enums.Language;
import com.canki.stockmanagement.productcacheservice.repository.entity.Product;
import com.canki.stockmanagement.productcacheservice.response.InternalApiResponse;
import com.canki.stockmanagement.productcacheservice.response.ProductResponse;
import com.canki.stockmanagement.productcacheservice.service.ProductRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/1.0/product-cache")
@Slf4j
@RequiredArgsConstructor
public class ProductCacheController {

    private final ProductRepositoryService productRepositoryService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value="{language}/get/{productId}")
    public InternalApiResponse<ProductResponse> getProduct(@PathVariable("language") Language language,
                                                           @PathVariable("productId") Long productId) {
        log.debug("[{}][getProduct] -> request productId: {}", this.getClass().getSimpleName(), productId);
        Product product = productRepositoryService.getProduct(language, productId);
        ProductResponse productResponse = convertProductResponse(product);
        log.debug("[{}][getProduct] -> response: {}", this.getClass().getSimpleName(), productResponse);
        return InternalApiResponse.<ProductResponse>builder()
                .httpStatus(HttpStatus.OK)
                .hasError(false)
                .payload(productResponse)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{language}/removeProducts")
    public void removeProducts(@PathVariable("language") Language language) {
        productRepositoryService.deleteProducts(language);
    }

    private static ProductResponse convertProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productCreatedDate(product.getProductCreatedDate())
                .productUpdatedDate(product.getProductUpdatedDate())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .build();
    }
}
