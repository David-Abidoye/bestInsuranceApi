package com.bestinsurance.api.mapper;

import static com.bestinsurance.api.helper.AddressHelper.areAllAddressFieldsNull;

import java.util.Optional;
import java.util.UUID;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.CustomerUpdateRequest;
import com.bestinsurance.api.model.Address;
import com.bestinsurance.api.model.City;
import com.bestinsurance.api.model.Country;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.State;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerUpdateMapper extends DTOMapper<CustomerUpdateRequest, Customer>{

    @Override
    @Mapping(target = "address", expression = "java(mapAddress(customerUpdateRequest))")
    Customer map(CustomerUpdateRequest customerUpdateRequest);

    @Named("mapAddress")
    @Mapping(target = "addressLine", source = "street")
    @Mapping(target = "city", source = "idCity", qualifiedByName = "mapCity")
    @Mapping(target = "state", source = "idState", qualifiedByName = "mapState")
    @Mapping(target = "country", source = "idCountry", qualifiedByName = "mapCountry")
    Address mapAddress(CustomerUpdateRequest customerUpdateRequest);

    @Named("mapCity")
    default City mapCity(String idCity) {
        return Optional.ofNullable(idCity).map(id -> City.builder().id(UUID.fromString(id)).build()).orElse(null);
    }

    @Named("mapState")
    default State mapState(String idState) {
        return Optional.ofNullable(idState).map(id -> State.builder().id(UUID.fromString(id)).build()).orElse(null);
    }

    @Named("mapCountry")
    default Country mapCountry(String idCountry) {
        return Optional.ofNullable(idCountry).map(id -> Country.builder().id(UUID.fromString(id)).build()).orElse(null);
    }

    @AfterMapping
    default void nullifyAddressIfEmpty(@MappingTarget Customer customer){
        if (areAllAddressFieldsNull(customer.getAddress())){
            customer.setAddress(null);
        }
    }
}
