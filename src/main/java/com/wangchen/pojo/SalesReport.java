package com.wangchen.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReport {
    private int reportID;
    private Date salesDate;
    private BigDecimal totalSales;
    private int totalQuantity;
}
