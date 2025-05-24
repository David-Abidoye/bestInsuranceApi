package com.bestinsurance.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.bestinsurance.api.dto.AddressView;
import com.bestinsurance.api.dto.CityView;
import com.bestinsurance.api.dto.CountryView;
import com.bestinsurance.api.dto.CustomerResponse;
import com.bestinsurance.api.dto.StateView;
import com.bestinsurance.api.model.Address;
import com.bestinsurance.api.model.City;
import com.bestinsurance.api.model.Country;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.State;

@Mapper(componentModel = "spring")
public interface CustomerResponseMapper extends DTOMapper<Customer, CustomerResponse> {

    @Override
    @Mapping(target = "address", source = "address")
    CustomerResponse map(Customer customer);

    @Mapping(target = "address", source = "addressLine")
    @Mapping(target = "country", source = "country", qualifiedByName = "mapCountryView")
    @Mapping(target = "state", source = "state", qualifiedByName = "mapStateView")
    @Mapping(target = "city", source = "city", qualifiedByName = "mapCityView")
    AddressView mapAddressView(Address address);

    @Named("mapCountryView")
    CountryView mapCountryView(Country country);

    @Named("mapStateView")
    StateView mapStateView(State state);

    @Named("mapCityView")
    CityView mapCityView(City city);

    default List<CustomerResponse> mapCustomers(List<Customer> customers) {
        return customers.stream()
                .map(this::map)
                .toList();
    }
}
