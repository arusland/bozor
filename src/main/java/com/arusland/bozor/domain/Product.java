package com.arusland.bozor.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ruslan on 21.07.2014.
 */
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    public Product() {
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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs == null || !(rhs instanceof Product) || getId() == null) {
            return false;
        }

        return getId().equals(((Product) rhs).getId());
    }
}
