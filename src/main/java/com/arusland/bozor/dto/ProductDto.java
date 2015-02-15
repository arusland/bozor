package com.arusland.bozor.dto;

import com.arusland.bozor.domain.Product;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ProductDto {
    private Long id;
    private String name;
    private Long typeId;

    public ProductDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs == null || !(rhs instanceof ProductDto) || getId() == null) {
            return false;
        }

        return getId().equals(((ProductDto) rhs).getId());
    }

    public Product toProduct() {
        Product product = new Product();

        product.setId(getId());
        product.setName(getName());

        return product;
    }

    public static ProductDto fromProduct(Product product) {
        ProductDto dto = new ProductDto();

        dto.setId(product.getId());
        dto.setTypeId(product.getProductType().getId());
        dto.setName(product.getName());

        return dto;
    }

    public static List<ProductDto> fromList(List<Product> products, boolean sort) {
        if (products == null) {
            return null;
        }

        List<ProductDto> result = new LinkedList<>();

        for (Product product : products) {
            result.add(fromProduct(product));
        }

        return sort ? sort(result) : result;
    }

    private static List<ProductDto> sort(List<ProductDto> list) {
        Collections.sort(list, new Comparator<ProductDto>() {
            @Override
            public int compare(ProductDto o1, ProductDto o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return list;
    }
}
