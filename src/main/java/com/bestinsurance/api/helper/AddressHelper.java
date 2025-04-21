package com.bestinsurance.api.helper;

import java.util.Optional;
import com.bestinsurance.api.model.Address;

public class AddressHelper {

    private AddressHelper(){}

    public static boolean isAnyAddressFieldPresent(Address address) {
        return Optional.ofNullable(address.getAddressLine()).isPresent() ||
                Optional.ofNullable(address.getPostalCode()).isPresent() ||
                Optional.ofNullable(address.getCity().getId()).isPresent() ||
                Optional.ofNullable(address.getState().getId()).isPresent() ||
                Optional.ofNullable(address.getCountry().getId()).isPresent();
    }

    public static boolean areAllAddressFieldsPresent(Address address) {
        return Optional.ofNullable(address.getAddressLine()).isPresent() &&
                Optional.ofNullable(address.getPostalCode()).isPresent() &&
                Optional.ofNullable(address.getCity().getId()).isPresent() &&
                Optional.ofNullable(address.getState().getId()).isPresent() &&
                Optional.ofNullable(address.getCountry().getId()).isPresent();
    }

    public static boolean areAllAddressFieldsNull(Address address) {
        return address.getAddressLine() == null &&
                address.getPostalCode() == null &&
                address.getCity() == null &&
                address.getState() == null &&
                address.getCountry() == null;
    }
}
