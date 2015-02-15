package com.arusland.bozor.service;

import com.arusland.bozor.domain.Product;
import com.arusland.bozor.domain.ProductItem;
import com.arusland.bozor.domain.ProductType;
import com.arusland.bozor.dto.ProductDto;
import com.arusland.bozor.dto.ProductItemDto;

import java.util.Date;
import java.util.List;

/**
 * Created by ruslan on 30.09.2014.
 */
public interface ProductService {

    Product save(ProductDto product);

    ProductItem save(ProductItemDto productItem);

    ProductType save(ProductType productType);

    void removeProductItem(Long id);

    void removeProduct(Long id);

    List<ProductItem> getProductItems(Date date, boolean getNew, Integer timezoneOffset);

    List<ProductItem> getProductItems(Date dateFrom, Date dateTo, boolean getNew, Integer timezoneOffset);

    List<Product> getProducts();

    List<ProductType> getProductTypes();

    ProductItemDto buyItem(Long id);

    List<ProductDto> searchProducts(String searchText);

    ProductItemDto addItem(Long productId);
}
