package com.bestinsurance.api.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Roles {
    public static final String ADMIN = "ADMIN";
    public static final String FRONT_OFFICE = "FRONT_OFFICE";
    public static final String BACK_OFFICE = "BACK_OFFICE";
    public static final String CUSTOMER = "CUSTOMER";
}
