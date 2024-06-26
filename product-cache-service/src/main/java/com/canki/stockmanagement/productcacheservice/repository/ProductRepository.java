package com.canki.stockmanagement.productcacheservice.repository;

import com.canki.stockmanagement.productcacheservice.repository.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends CrudRepository<Product, Long> {

}
