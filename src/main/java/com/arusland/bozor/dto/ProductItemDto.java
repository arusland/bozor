package com.arusland.bozor.dto;

import com.arusland.bozor.domain.Product;
import com.arusland.bozor.domain.ProductItem;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruslan on 02.10.2014.
 */
public class ProductItemDto {
    private Long id;
    private Long productId;
    private Double price;
    private Date date;
    private Double amount;
    private String comment;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ProductItem toItem(){
        ProductItem item = new ProductItem();

        item.setId(getId());
        item.setDate(getDate());
        item.setAmount(getAmount());
        item.setComment(getComment());
        item.setPrice(getPrice());

        return item;
    }

    public static ProductItemDto fromItem(ProductItem item){
        ProductItemDto dto = new ProductItemDto();

        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setAmount(item.getAmount());
        dto.setComment(item.getComment());
        dto.setPrice(item.getPrice());
        dto.setName(item.getProduct().getName());

        return dto;
    }

    public static List<ProductItemDto> fromList(List<ProductItem> items){
        if (items == null){
            return null;
        }

        List<ProductItemDto> result = new LinkedList<>();

        for (ProductItem item : items){
            result.add(fromItem(item));
        }

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
