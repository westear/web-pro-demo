package com.souche;

import lombok.Data;

@Data
public class CommonOrder {

    private Long id;

    private String baseOrderCode;

    private int orderType;

    private String carId;

    private String shopCode;

    private String plateNumber;

    private String carSeries;

    private String carBrand;
}
