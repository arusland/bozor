package com.arusland.bozor.api;

import com.arusland.bozor.domain.*;
import com.arusland.bozor.dto.*;
import com.arusland.bozor.service.ProductService;
import com.arusland.bozor.service.StatusManager;
import com.arusland.bozor.util.DateUtils;
import com.arusland.bozor.util.ExpressionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public Status getStatusToday(@PathVariable String token,
                                 @RequestHeader(value = "Bzr-TimeOffset", required = false) Integer timeOffset) throws InterruptedException {
        Date now = DateUtils.toUTCTime(new Date(), timeOffset);
        return getStatus(token, DateUtils.toStringShort(now), timeOffset);
    }

    @ResponseBody
    @RequestMapping("/status/{token}/{time}")
    public Status getStatus(@PathVariable String token, @PathVariable String time,
                            @RequestHeader(value = "Bzr-TimeOffset", required = false) Integer timeOffset) throws InterruptedException {
        Status status;
        StatusResult statusResult = statusManager.hasUpdates(token);

        if (statusResult.hasUpdates) {
            Date parsedTime = DateUtils.parseTime(time);
            boolean isToday = DateUtils.isToday(parsedTime, timeOffset);

            List<ProductItem> items = statusResult.hasNewItems ? service.getProductItems(parsedTime, isToday, timeOffset) : null;
            List<Product> products = statusResult.hasNewProducts ? service.getProducts() : null;

            status = new Status(statusResult.token, ProductItemDto.fromList(items),
                    ProductDto.fromList(products));
        } else {
            status = new Status(statusResult.token);
        }

        return status;
    }

    @ResponseBody
    @RequestMapping("/chart/pmp/{month}")
    public List<ChartDataDto> getPieChartByProductInMonth(@PathVariable String month,
                                                          @RequestHeader(value = "Bzr-TimeOffset", required = false) Integer timeOffset) {
        Date monthParsed = DateUtils.parseMonth(month);
        Date timeFrom = DateUtils.getMinTimeOfMonth(monthParsed);
        Date timeTo = DateUtils.getMaxTimeOfMonth(monthParsed);
        List<ProductItem> productItems = service.getProductItems(timeFrom, timeTo, false, timeOffset);
        HashMap<Product, Double> items = new HashMap<>();

        for (ProductItem item : productItems) {
            Double inner = items.get(item.getProduct());

            if (inner == null) {
                inner = new Double(0);
            }

            inner += calcPrice(item.getPrice());
            items.put(item.getProduct(), inner);
        }

        LinkedList<ChartDataDto> result = new LinkedList<>();

        for (Map.Entry<Product, Double> entry : items.entrySet()) {
            result.add(new ChartDataDto(entry.getKey().getName(), entry.getValue()));
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/chart/pmt/{month}")
    public List<ChartDataDto> getPieChartByProductTypeInMonth(@PathVariable String month,
                                                              @RequestHeader(value = "Bzr-TimeOffset", required = false) Integer timeOffset) {
        Date monthParsed = DateUtils.parseMonth(month);
        Date timeFrom = DateUtils.getMinTimeOfMonth(monthParsed);
        Date timeTo = DateUtils.getMaxTimeOfMonth(monthParsed);
        List<ProductItem> productItems = service.getProductItems(timeFrom, timeTo, false, timeOffset);
        HashMap<ProductType, Double> items = new HashMap<>();

        for (ProductItem item : productItems) {
            ProductType type = item.getProduct().getProductType();
            Double inner = items.get(type);

            if (inner == null) {
                inner = new Double(0);
            }

            inner += calcPrice(item.getPrice());
            items.put(type, inner);
        }

        LinkedList<ChartDataDto> result = new LinkedList<>();

        for (Map.Entry<ProductType, Double> entry : items.entrySet()) {
            result.add(new ChartDataDto(entry.getKey().getName(), entry.getValue()));
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/itemsMonth/{month}")
    public List<StatusMonth> getItemsByMonth(@PathVariable String month,
                                             @RequestHeader(value = "Bzr-TimeOffset", required = false) Integer timeOffset) {
        Date monthParsed = DateUtils.parseMonth(month);
        Date timeFrom = DateUtils.getMinTimeOfMonth(monthParsed);
        Date timeTo = DateUtils.getMaxTimeOfMonth(monthParsed);
        List<ProductItem> productItems = service.getProductItems(timeFrom, timeTo, false, timeOffset);
        List<StatusMonth> result = new LinkedList<>();

        for (ProductItem item : productItems) {
            String key = DateUtils.toStringShort(item.getDate());
            StatusMonth status = getOrCreateStatus(result, key);

            status.getItems().add(ProductItemDto.toDtoShort(item));
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/products")
    public List<ProductDto> getProducts() {
        return ProductDto.fromList(service.getProducts());
    }

    @ResponseBody
    @RequestMapping("/types")
    public List<ProductType> getProductTypes() {
        return service.getProductTypes();
    }

    @ResponseBody
    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public ProductItemDto updateItem(@RequestBody ProductItemDto item) {
        return ProductItemDto.fromItem(service.save(item));
    }

    @ResponseBody
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return ProductDto.fromProduct(service.save(productDto));
    }

    @ResponseBody
    @RequestMapping(value = "/type", method = RequestMethod.POST)
    public ProductType updateProduct(@RequestBody ProductType productType) {
        return service.save(productType);
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.DELETE)
    public void removeItem(@PathVariable Long id) {
        if (id != null) {
            service.removeProductItem(id);
        }
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public void removeProduct(@PathVariable Long id) {
        if (id != null) {
            service.removeProduct(id);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/buy/{itemId}", method = RequestMethod.POST)
    public ProductItemDto buyItem(@PathVariable Long itemId) {
        return service.buyItem(itemId);
    }

    private static StatusMonth getOrCreateStatus(List<StatusMonth> list, String key) {
        for (int i = 0; i < list.size(); i++) {
            if (key.equals(list.get(i).getDay())) {
                return list.get(i);
            }
        }

        StatusMonth status = new StatusMonth(key, new LinkedList<ProductItemShort>());

        list.add(status);

        return status;
    }

    private double calcPrice(String price) {
        return ExpressionUtils.eval(price);
    }
}
