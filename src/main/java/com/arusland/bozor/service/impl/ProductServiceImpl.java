package com.arusland.bozor.service.impl;

import com.arusland.bozor.domain.Product;
import com.arusland.bozor.domain.ProductItem;
import com.arusland.bozor.dto.ProductItemDto;
import com.arusland.bozor.repository.ProductItemRepository;
import com.arusland.bozor.repository.ProductRepository;
import com.arusland.bozor.service.ProductService;
import com.arusland.bozor.service.StatusManager;
import com.arusland.bozor.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;

/**
 * Created by ruslan on 30.09.2014.
 */
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final StatusManager statusManager;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductItemRepository productItemRepository, StatusManager statusManager) {
        this.productRepository = productRepository;
        this.productItemRepository = productItemRepository;
        this.statusManager = statusManager;
    }

    @Override
    public Product save(Product product) {
        statusManager.modifyProducts();
        return productRepository.save(product);
    }

    @Override
    public ProductItem save(ProductItemDto productItem) {
        statusManager.modifyItems();

        ProductItem item = productItem.toItem();
        item.setProduct(productRepository.getOne(productItem.getProductId()));

        return productItemRepository.save(item);
    }

    @Override
    public void removeProductItem(Long id) {
        statusManager.modifyItems();
        productItemRepository.delete(id);
    }

    @Override
    public List<ProductItem> getProductItems(Date date, boolean getNew) {
        Date dateFrom = DateUtils.getMinTimeOfDay(date);
        Date dateTo = DateUtils.getMaxTimeOfDay(date);

        return getProductItems(dateFrom, dateTo, getNew);
    }

    @Override
    public List<ProductItem> getProductItems(Date dateFrom, Date dateTo, boolean getNew) {
        return productItemRepository.getItemsBetweenDates(dateFrom, dateTo, getNew ? 1 : 0);
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public ProductItemDto buyItem(Long id) {
        ProductItem item = productItemRepository.getOne(id);

        if (item != null && item.getDate() == null) {
            statusManager.modifyItems();
            item.setDate(new Date());

            item = productItemRepository.save(item);
        }

        return ProductItemDto.fromItem(item);
    }
}
