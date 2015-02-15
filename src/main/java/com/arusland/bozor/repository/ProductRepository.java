package com.arusland.bozor.repository;

import com.arusland.bozor.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ruslan on 30.09.2014.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.name like %?1%")
    List<Product> searchByName(String text);
}
