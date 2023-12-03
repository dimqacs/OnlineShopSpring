package com.demo.app.model;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequest {

    private List<PurchaseItemDTO> purchase;

    private Float purchaseTotalPrice;
}
