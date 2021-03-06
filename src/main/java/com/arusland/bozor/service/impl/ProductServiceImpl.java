package com.arusland.bozor.service.impl;

import com.arusland.bozor.domain.Product;
import com.arusland.bozor.domain.ProductItem;
import com.arusland.bozor.domain.ProductType;
import com.arusland.bozor.dto.ProductDto;
import com.arusland.bozor.dto.ProductItemDto;
import com.arusland.bozor.repository.ProductItemRepository;
import com.arusland.bozor.repository.ProductRepository;
import com.arusland.bozor.repository.ProductTypeRepository;
import com.arusland.bozor.service.ProductService;
import com.arusland.bozor.service.StatusManager;
import com.arusland.bozor.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ruslan on 30.09.2014.
 */
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductTypeRepository productTypeRepository;
    private final StatusManager statusManager;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductItemRepository productItemRepository, ProductTypeRepository productTypeRepository, StatusManager statusManager) {
        this.productRepository = productRepository;
        this.productItemRepository = productItemRepository;
        this.productTypeRepository = productTypeRepository;
        this.statusManager = statusManager;
    }

    @Override
    @Transactional
    public Product save(ProductDto productDto) {
        statusManager.modifyProducts();

        Product product = productDto.toProduct();
        ProductType productType = productDto.getTypeId() != null ?
                productTypeRepository.getOne(productDto.getTypeId()) :
                productTypeRepository.getOne(1L); // if product type not specifie set defualt (first) type

        product.setProductType(productType);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductItem save(ProductItemDto productItem) {
        statusManager.modifyItems();

        ProductItem item = productItem.toItem();

        // buy item if it has price and not bought yet
        if (StringUtils.isNotBlank(item.getPrice()) && item.getDate() == null) {
            setItemBought(item);
        }
        item.setProduct(productRepository.getOne(productItem.getProductId()));

        return productItemRepository.save(item);
    }

    @Override
    @Transactional
    public ProductType save(ProductType productType) {
        if (StringUtils.isBlank(productType.getName())) {
            throw new RuntimeException("Product type must have name");
        }

        ProductType type = productTypeRepository.findByName(productType.getName());

        if (type != null && !type.getId().equals(productType.getId())) {
            throw new RuntimeException("Product with the same name already exists");
        }

        return productTypeRepository.save(productType);
    }

    @Override
    public void removeProductItem(Long id) {
        statusManager.modifyItems();
        productItemRepository.delete(id);
    }

    @Override
    public void removeProduct(Long id) {
        productRepository.delete(id);
    }

    @Override
    public List<ProductItem> getProductItems(Date date, boolean getNew, Integer timezoneOffset) {
        Date dateFrom = DateUtils.getMinTimeOfDay(date);
        Date dateTo = DateUtils.getMaxTimeOfDay(date);

        return getProductItems(dateFrom, dateTo, getNew, timezoneOffset);
    }

    @Override
    public List<ProductItem> getProductItems(Date dateFrom, Date dateTo, boolean getNew, Integer timezoneOffset) {
        if (timezoneOffset != null) {
            dateFrom = DateUtils.addMinutes(dateFrom, timezoneOffset);
            dateTo = DateUtils.addMinutes(dateTo, timezoneOffset);
        }

        return productItemRepository.getItemsBetweenDates(dateFrom, dateTo, getNew ? 1 : 0);
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductType> getProductTypes() {
        return productTypeRepository.findAll();
    }

    @Override
    @Transactional
    public ProductItemDto buyItem(Long id) {
        ProductItem item = productItemRepository.getOne(id);

        if (item != null && item.getDate() == null) {
            statusManager.modifyItems();
            setItemBought(item);
            item = productItemRepository.save(item);
        }

        return ProductItemDto.fromItem(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> searchProducts(String searchText) {
        List<Product> products = productRepository.searchByName(searchText);

        Collections.sort(products, new SearchProductComparator(searchText));

        return ProductDto.fromList(products, false);
    }

    @Override
    @Transactional
    public ProductItemDto addItem(Long productId) {
        Product product = productRepository.getOne(productId);

        if (product != null) {
            statusManager.modifyItems();
            ProductItem item = new ProductItem();
            item.setProduct(product);
            item = productItemRepository.saveAndFlush(item);

            return ProductItemDto.fromItem(item);
        }

        throw new IllegalStateException("Product not found: " + productId);
    }

    @Override
    @Transactional
    public ProductItemDto addItem(String productName) {
        productName = productName.toLowerCase();
        Product product = productRepository.findByName(productName);

        if (product == null){
            // product not found, create it
            product = save(new ProductDto(productName));
        }

        return addItem(product.getId());
    }

    private static void setItemBought(ProductItem item) {
        item.setDate(DateUtils.toUTCTime(new Date()));
    }
}
