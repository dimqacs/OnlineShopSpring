package com.demo.app.model;

import lombok.Data;

@Data
public class PurchaseItemDTO {

    private Long id;

    private String productName;

    private String productDetails;

    private Integer count;

    private Float totalPrice;

    private Long purchaseId;
}
