package com.arusland.bozor.repository;

import com.arusland.bozor.domain.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ruslan on 15.10.2014.
 */
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    ProductType findByName(String name);
}
