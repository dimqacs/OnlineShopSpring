package com.demo.app.model;

import lombok.Data;

@Data
public class ProductCreateDTO {

    private String name;

    private Integer yearOfProduction;

    private String details;

    private Float price;

    private Integer count;

    private Long categoryId;

    private Long shipperId;
}
