package com.wangchen.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderID;
    private int userID;
    private String orderNumber;
    private BigDecimal totalPrice;
    private String orderStatus;
}
