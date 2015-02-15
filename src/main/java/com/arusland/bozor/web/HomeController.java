package com.arusland.bozor.web;

import com.arusland.bozor.dto.ProductDto;
import com.arusland.bozor.dto.ProductItemDto;
import com.arusland.bozor.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * Created by ruslan on 29.09.2014.
 */
@Controller
public class HomeController {
    private final ProductService service;

    @Autowired
    public HomeController(ProductService service) {
        this.service = service;
    }

    @RequestMapping("/")
    public String index() {
        return "home";
    }

    @RequestMapping("/show")
    public String show() {
        return "show";
    }

    @RequestMapping("/products")
    public String products() {
        return "products";
    }

    @RequestMapping("/chart")
    public String chart() {
        return "chart";
    }

    @RequestMapping("/m")
    public String mobile(@RequestParam(value = "s", required = false) String searchText, Map<String, Object> model) {
        // TODO: provide client timezone offset
        int timeOffset = 0;
        List<ProductItemDto> items = ProductItemDto.fromList(service.getProductItems(new Date(), true, timeOffset));
        model.put("items", items);
        model.put("showComments", hasComments(items));

        if (StringUtils.isNotBlank(searchText)){
            List<ProductDto> products = service.searchProducts(searchText);

            model.put("foundProducts", products);
        }

        return "mobile";
    }

    @RequestMapping("/buy/{id}")
    public String mobileBuyItem(@PathVariable Long id) {
        service.buyItem(id);

        return "redirect:/m";
    }

    private boolean hasComments(List<ProductItemDto> items) {
        for (ProductItemDto item : items) {
            if (StringUtils.isNotBlank(item.getComment())
                    || item.getAmount() != null) {
                return true;
            }
        }

        return false;
    }
}
