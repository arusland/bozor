package com.arusland.bozor.domain;

import javax.persistence.*;

/**
 * Created by ruslan on 13.10.2014.
 */
@Entity
@Table(name="product_type")
public class ProductType {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

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
}
