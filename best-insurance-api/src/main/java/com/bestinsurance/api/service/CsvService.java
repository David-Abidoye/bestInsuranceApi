package com.bestinsurance.api.service;

import static com.bestinsurance.api.validation.utils.ValidationUtils.parseDate;
import static com.bestinsurance.api.validation.utils.ValidationUtils.parsePrice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.bestinsurance.api.dto.SubscriptionCsv;
import com.bestinsurance.api.model.Address;
import com.bestinsurance.api.model.City;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.embedded.SubscriptionId;
import com.bestinsurance.api.repos.CityRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CsvService {

    private final CityRepository cityRepository;
    private final PolicyRepository policyRepository;
    private final CustomerService customerService;
    private final SubscriptionService subscriptionService;

    public CsvService(CityRepository cityRepository, PolicyRepository policyRepository, CustomerService customerService, SubscriptionService subscriptionService) {
        this.cityRepository = cityRepository;
        this.policyRepository = policyRepository;
        this.customerService = customerService;
        this.subscriptionService = subscriptionService;
    }

    @Transactional(rollbackFor = Exception.class)
    public String upload(MultipartFile file) {
        try {
            List<SubscriptionCsv> parsedSubscriptionCsv = parseCsv(file);
            int count = 0;
            for (SubscriptionCsv subscriptionCsv : parsedSubscriptionCsv) {
                saveSubscriptionCsv(subscriptionCsv);
                count++;
            }
            return String.format("%d subscription records have been successfully added to database.", count);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing uploaded csv file with error: " + e.getMessage());
        }
    }

    private void saveSubscriptionCsv(SubscriptionCsv subscriptionCsv) {
        String policyName = subscriptionCsv.getPolicyName();
        Policy existingPolicy = policyRepository.findByName(policyName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Policy with name [%s] does not exist!", policyName)));
        String cityName = subscriptionCsv.getCityName();
        String stateName = subscriptionCsv.getStateName();
        City existingCity = cityRepository.findByNameAndStateName(cityName, stateName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("City with name [%s] in State with name [%s] does not exist!", cityName, stateName)));
        String email = subscriptionCsv.getEmail();
        Customer existingCustomer = customerService.getByEmail(email)
                .orElse(null);
        Address addressCsv = Address.builder()
                .addressLine(subscriptionCsv.getAddress())
                .postalCode(subscriptionCsv.getPostalCode())
                .city(existingCity)
                .state(existingCity.getState())
                .country(existingCity.getCountry())
                .build();
        Customer customerCsv = Customer.builder()
                .name(subscriptionCsv.getName())
                .surname(subscriptionCsv.getSurname())
                .birthDate(parseDate(subscriptionCsv.getBirthDate()))
                .email(email)
                .address(addressCsv)
                .build();
        Customer createdOrUpdatedCustomer = Optional.ofNullable(existingCustomer)
                .map(customer -> customerService.update(customer, customerCsv))
                .orElseGet(() -> customerService.create(customerCsv));

        Subscription subscription = Subscription.builder()
                .id(SubscriptionId.builder()
                        .customerId(createdOrUpdatedCustomer.getId())
                        .policyId(existingPolicy.getId())
                        .build())
                .customer(createdOrUpdatedCustomer)
                .policy(existingPolicy)
                .paidPrice(parsePrice(subscriptionCsv.getPaidPrice()))
                .startDate(parseDate(subscriptionCsv.getStartDate()))
                .endDate(parseDate(subscriptionCsv.getEndDate()))
                .build();
        subscriptionService.create(subscription);
    }

    private List<SubscriptionCsv> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            HeaderColumnNameMappingStrategy<SubscriptionCsv> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(SubscriptionCsv.class);
            CsvToBean<SubscriptionCsv> csvToBean = new CsvToBeanBuilder<SubscriptionCsv>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(';')
                    .build();
            return csvToBean.parse();
        }
    }
}
