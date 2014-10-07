package com.arusland.bozor.api;

import com.arusland.bozor.domain.*;
import com.arusland.bozor.dto.*;
import com.arusland.bozor.service.ProductService;
import com.arusland.bozor.service.StatusManager;
import com.arusland.bozor.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruslan on 30.09.2014.
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    private final ProductService service;
    private final StatusManager statusManager;

    @Autowired
    public ApiController(ProductService service, StatusManager statusManager) {
        this.service = service;
        this.statusManager = statusManager;
    }

    @ResponseBody
    @RequestMapping("/status/{token}")
    public Status getStatusToday(@PathVariable String token) {
        return getStatus(token, DateUtils.toStringShort(new Date()));
    }

    @ResponseBody
    @RequestMapping("/status/{token}/{time}")
    public Status getStatus(@PathVariable String token, @PathVariable String time) {
        Status status;
        StatusResult statusResult = statusManager.hasUpdates(token);

        if (statusResult.hasUpdates) {
            Date parsedTime = DateUtils.parseTime(time);
            boolean isToday = DateUtils.isToday(parsedTime);

            List<ProductItem> items = statusResult.hasNewItems ? service.getProductItems(parsedTime, isToday) : null;
            List<Product> products = statusResult.hasNewProducts ? service.getProducts() : null;

            status = new Status(statusResult.token, ProductItemDto.fromList(items), products);
        } else {
            status = new Status(statusResult.token);
        }

        return status;
    }


    public List<ProductItem> getItems(@PathVariable String time) {
        Date parsedTime = DateUtils.parseTime(time);

        return service.getProductItems(parsedTime, true);
    }

    @ResponseBody
    @RequestMapping("/itemsMonth/{month}")
    public List<StatusMonth> getItemsByMonth(@PathVariable String month)
    {
        Date monthParsed = DateUtils.parseMonth(month);
        Date timeFrom = DateUtils.getMinTimeOfMonth(monthParsed);
        Date timeTo = DateUtils.getMaxTimeOfMonth(monthParsed);
        List<ProductItem> productItems = service.getProductItems(timeFrom, timeTo, false);
        List<StatusMonth> result = new LinkedList<>();

        for (ProductItem item : productItems){
            String key = DateUtils.toStringShort(item.getDate());
            StatusMonth status = getOrCreateStatus(result, key);

            status.getItems().add(ProductItemDto.toDtoShort(item));
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/products")
    public List<Product> getProducts() {
        return service.getProducts();
    }

    @ResponseBody
    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public ProductItemDto updateItem(@RequestBody ProductItemDto item){
        return ProductItemDto.fromItem(service.save(item));
    }

    @ResponseBody
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public Product updateProduct(@RequestBody Product item) {
        return service.save(item);
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.DELETE)
    public void removeItem(@PathVariable Long id) {
        if (id != null) {
            service.removeProductItem(id);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/buy/{itemId}", method = RequestMethod.POST)
    public ProductItemDto buyItem(@PathVariable Long itemId) {
        return service.buyItem(itemId);
    }

    private static StatusMonth getOrCreateStatus(List<StatusMonth> list, String key)
    {
        for (int i = 0;i < list.size();i++){
            if (key.equals(list.get(i).getDay())){
                return list.get(i);
            }
        }

        StatusMonth status = new StatusMonth(key, new LinkedList<ProductItemShort>());

        list.add(status);

        return status;
    }
}
