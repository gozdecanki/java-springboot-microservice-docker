package com.canki.stockmanagement.productservice.service.Impl;

import com.canki.stockmanagement.productservice.enums.Language;
import com.canki.stockmanagement.productservice.exception.enums.FriendlyMessageCodes;
import com.canki.stockmanagement.productservice.exception.exceptions.ProductAlreadyDeletedException;
import com.canki.stockmanagement.productservice.exception.exceptions.ProductNotCreatedException;
import com.canki.stockmanagement.productservice.exception.exceptions.ProductNotFoundException;
import com.canki.stockmanagement.productservice.repository.entity.Product;
import com.canki.stockmanagement.productservice.repository.entity.ProductRepository;
import com.canki.stockmanagement.productservice.request.ProductCreateRequest;
import com.canki.stockmanagement.productservice.request.ProductUpdateRequest;
import com.canki.stockmanagement.productservice.service.IProductRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRepositoryServiceImpl implements IProductRepositoryService {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Language language, ProductCreateRequest productCreateRequest) {
        log.debug("[{}][createProduct] ->  request : {}", this.getClass().getSimpleName(), productCreateRequest);
        try {
            Product product = Product.builder()
                    .productName(productCreateRequest.getProductName())
                    .quantity(productCreateRequest.getQuantity())
                    .price(productCreateRequest.getPrice())
                    .deleted(false)
                    .build();
            Product productResponse = productRepository.save(product);
            log.debug("[{}][createProduct] ->  response : {}", this.getClass().getSimpleName(), productResponse);
            return productResponse;
        } catch (Exception exception) {
            throw new ProductNotCreatedException(language,
                    FriendlyMessageCodes.PRODUCT_NOT_CREATED_EXCEPTION, "product reques: " + productCreateRequest.toString());
        }

    }

    @Override
    public Product getProduct(Language language, Long productId) {
        log.debug("[{}][getProduct] ->  request : {}", this.getClass().getSimpleName(), productId);
        Product product = productRepository.getByProductIdAndDeletedFalse(productId);
        if (Objects.isNull(product)) {
            throw new ProductNotFoundException(language, FriendlyMessageCodes.PRODUCT_NOT_FOUND_EXCEPTION, "Product not found for product id: " + productId);
        }
        log.debug("[{}][getProduct] ->  response : {}", this.getClass().getSimpleName(), product);
        return product;
    }

    @Override
    public List<Product> getProducts(Language language) {
        log.debug("[{}][getProducts] ->  request : {}", this.getClass().getSimpleName());

        List<Product> products = productRepository.getAllByDeletedFalse();

        if (products.isEmpty()) {
            throw new ProductNotFoundException(language, FriendlyMessageCodes.PRODUCT_NOT_FOUND_EXCEPTION, "Products not found.");
        }
        log.debug("[{}][getProducts] ->  response : {}", this.getClass().getSimpleName(), products);
        return products;
    }

    @Override
    public Product updateProduct(Language language, Long productId, ProductUpdateRequest productUpdateRequest) {
        log.debug("[{}][updateProduct] ->  request : {}", this.getClass().getSimpleName(), productUpdateRequest);

        Product product = getProduct(language, productId);
        product.setProductName(productUpdateRequest.getProductName());
        product.setQuantity(productUpdateRequest.getQuantity());
        product.setPrice(productUpdateRequest.getPrice());
        product.setProductCreatedDate(product.getProductCreatedDate());
        product.setProductUpdatedDate(new Date());

        Product productResponse = productRepository.save(product);
        log.debug("[{}][updateProduct] ->  response : {}", this.getClass().getSimpleName(), productResponse);

        return productResponse;
    }

    @Override
    public Product deleteProduct(Language language, Long productId) {
        log.debug("[{}][deleteProduct] ->  request : {}", this.getClass().getSimpleName(), productId);
        Product product;

        try {
            product = getProduct(language, productId);
            product.setDeleted(true);
            product.setProductUpdatedDate(new Date());
            Product productResponse = productRepository.save(product);
            log.debug("[{}][deleteProduct] ->  response : {}", this.getClass().getSimpleName(), productResponse);

            return  productResponse;

        } catch (ProductNotFoundException productNotFoundException) {
            throw new ProductAlreadyDeletedException(language, FriendlyMessageCodes.PRODUCT_ALLREADY_DELETED,
                    "Product already deleted product id: " + productId);
        }

    }
}
