package com.arusland.bozor.service.impl;

import com.arusland.bozor.domain.Product;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

/**
 * Created by Ruslan A. on 15.02.2015.
 */
public class SearchProductComparator implements Comparator<Product> {
    private final String searchText;

    public SearchProductComparator(final String searchText) {
        this.searchText = StringUtils.defaultString(searchText).toLowerCase();
    }

    @Override
    public int compare(Product o1, Product o2) {
        final String name1 = o1.getName().toLowerCase();
        final String name2 = o2.getName().toLowerCase();
        final boolean start1 = name1.startsWith(searchText);
        final boolean start2 = name2.startsWith(searchText);

        if (start1) {
            if (!start2) {
                return -1;
            }
        } else {
            if (start2) {
                return 1;
            }
        }

        return name1.compareTo(name2);
    }
}
