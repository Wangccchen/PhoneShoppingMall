package com.wangchen.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutItem {
    private Product product;
    private int quantity;
    private BigDecimal totalPrice;

}

