package com.arusland.bozor.repository;

import com.arusland.bozor.domain.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;

/**
 * Created by ruslan on 30.09.2014.
 */
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

    @Query("select c from ProductItem c where (c.date <= ?1 and c.date <= ?2) or ((c.date is null) and (1 = ?3))")
    List<ProductItem> getItemsBetweenDates(Date from, Date to, int isNew);
}
