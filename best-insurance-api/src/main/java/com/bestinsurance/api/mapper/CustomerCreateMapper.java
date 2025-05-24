package com.bestinsurance.api.mapper;

import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.CustomerCreateRequest;
import com.bestinsurance.api.model.Address;
import com.bestinsurance.api.model.City;
import com.bestinsurance.api.model.Country;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.State;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerCreateMapper extends DTOMapper<CustomerCreateRequest, Customer> {

    @Override
    @Mapping(target = "address", expression = "java(mapAddress(customerCreateRequest))")
    Customer map(CustomerCreateRequest customerCreateRequest);

    @Named("mapAddress")
    @Mapping(target = "addressLine", source = "street")
    @Mapping(target = "city", source = "idCity", qualifiedByName = "mapCity")
    @Mapping(target = "state", source = "idState", qualifiedByName = "mapState")
    @Mapping(target = "country", source = "idCountry", qualifiedByName = "mapCountry")
    Address mapAddress(CustomerCreateRequest customerCreateRequest);

    @Named("mapCity")
    default City mapCity(String idCity) {
        return City.builder().id(UUID.fromString(idCity)).build();
    }

    @Named("mapState")
    default State mapState(String idState) {
        return State.builder().id(UUID.fromString(idState)).build();
    }

    @Named("mapCountry")
    default Country mapCountry(String idCountry) {
        return Country.builder().id(UUID.fromString(idCountry)).build();
    }
}
