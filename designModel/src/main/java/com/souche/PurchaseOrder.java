package com.souche;

import java.util.Date;

public class PurchaseOrder extends CommonOrder {

    private Long id;

    private String baseOrderCode;

    private String carId;

    private String shopCode;

    private int purchaseType;

    private Date purchaseBeginDate;

    private Date purchaseEndDate;
}
