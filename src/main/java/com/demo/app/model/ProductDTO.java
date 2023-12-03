package com.demo.app.model;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;

    private String name;

    private Integer yearOfProduction;

    private String details;

    private Float price;

    private Integer count;

    private String categoryName;

    private String shipperName;

    private String shipperCountry;

    private Integer shipperYearOfFoundation;

    private String shipperDirectorName;
}
