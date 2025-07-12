package com.newdev.inservice.models;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString

public class ShopOwner {

    private String shopAddress;

    private int shopLicenceNumber;
}
