package com.arusland.bozor.service;

import com.arusland.bozor.domain.Product;
import com.arusland.bozor.domain.ProductItem;
import com.arusland.bozor.dto.ProductItemDto;

import java.util.Date;
import java.util.List;

/**
 * Created by ruslan on 30.09.2014.
 */
public interface ProductService {

    Product save(Product product);

    ProductItem save(ProductItemDto productItem);

    void removeProductItem(Long id);

    List<ProductItem> getProductItems(Date date, boolean getNew);

    List<ProductItem> getProductItems(Date dateFrom, Date dateTo, boolean getNew);

    List<Product> getProducts();

    ProductItemDto buyItem(Long id);
}
