package com.arusland.bozor.dto;

/**
 * Created by ruslan on 12.10.2014.
 */
public class ChartDataDto {
    private String name;
    private Double y;

    public ChartDataDto() {
    }

    public ChartDataDto(String name, double y) {
        this.name = name;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
