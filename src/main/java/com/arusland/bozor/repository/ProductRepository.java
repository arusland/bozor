package com.arusland.bozor.repository;

import com.arusland.bozor.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ruslan on 30.09.2014.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
