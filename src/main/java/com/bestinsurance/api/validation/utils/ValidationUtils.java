package com.bestinsurance.api.validation.utils;

import static com.bestinsurance.api.helper.ConstraintHelper.DATE_PATTERN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import com.bestinsurance.api.service.CustomerService;
import com.bestinsurance.api.service.PolicyService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {

    public static BigDecimal parsePriceFilter(Map<String, String> filters, String key) {
        return parsePrice(filters.get(key));
    }

    public static BigDecimal parsePrice(String price) {
        return Optional.ofNullable(price)
                .map(ValidationUtils::validateAndReturnPrice)
                .orElse(null);
    }

    public static PolicyService.PolicyOrderBy parsePolicyOrderByFilter(String orderBy) {
        return Optional.ofNullable(orderBy)
                .map(o -> {
                    if (!(o.equals("NAME") || o.equals("PRICE"))) {
                        throw new IllegalArgumentException("Invalid parameter to order policies by: " + o);
                    }
                    return PolicyService.PolicyOrderBy.valueOf(o);
                })
                .orElse(null);
    }

    public static String parseString(String stringToParse) {
        return Optional.ofNullable(stringToParse)
                .map(s -> {
                    if (!s.isBlank()) {
                        return s;
                    } else {
                        throw new IllegalArgumentException(String.format("%s cannot be blank", stringToParse));
                    }
                })
                .orElse(null);
    }

    public static String parseStringFilter(Map<String, String> filters, String key) {
        return parseString(filters.get(key));
    }

    public static Integer parseIntegerFilter(Map<String, String> filters, String key) {
        return Optional.ofNullable(filters.get(key))
                .map(s -> {
                    try {
                        Integer integer = Integer.valueOf(s);
                        if (integer < 1) {
                            throw new IllegalArgumentException(String.format("%s cannot be zero or negative", key));
                        }
                        return integer;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(String.format("%s is not a valid representation of an integer number", key));
                    }
                })
                .orElse(null);
    }

    public static CustomerService.CustomerOrderBy parseCustomerOrderByFilter(String orderBy) {
        return Optional.ofNullable(orderBy)
                .map(o -> {
                    if (!(o.equals("NAME") || o.equals("SURNAME") || o.equals("EMAIL") || o.equals("AGE"))) {
                        throw new IllegalArgumentException("Invalid parameter to order customers by: " + o);
                    }
                    return CustomerService.CustomerOrderBy.valueOf(o);
                })
                .orElse(null);
    }

    public static Sort.Direction parseOrderDirection(String orderDirection) {
        return Optional.ofNullable(orderDirection)
                .map(o -> {
                    if (!(o.equals("ASC") || o.equals("DESC"))) {
                        throw new IllegalArgumentException(String.format("Invalid parameter to define direction: %s. Accepted values are ASC and DESC: ", o));
                    }
                    return Sort.Direction.valueOf(o);
                })
                .orElse(null);
    }

    public static LocalDate parseDate(String dateToParse) {
        return Optional.ofNullable(dateToParse)
                .map(date -> {
                    try {
                        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN));
                    } catch (DateTimeParseException ex) {
                        throw new IllegalArgumentException(String.format("Date [%s] not properly formatted according to pattern yyyy-MM-dd", dateToParse));
                    }
                })
                .orElse(null);
    }

    public static void validateAgeFilter(Integer ageFrom, Integer ageTo) {
        if (!(ageFrom == null && ageTo == null) && (ageFrom == null || ageTo == null)) {
            throw new IllegalArgumentException("When searching by age, ageFrom and ageTo are mandatory");
        }
        if (ageFrom != null && ageFrom > ageTo) {
            throw new IllegalArgumentException("Not valid: ageFrom > ageTo");
        }
    }

    private static BigDecimal validateAndReturnPrice(String price) {
        try {
            BigDecimal bigDecimalPrice = new BigDecimal(price);
            if (bigDecimalPrice.scale() > 2 || bigDecimalPrice.precision() - bigDecimalPrice.scale() > 4) {
                throw new IllegalArgumentException("Price entry [" + price + "] is out of bounds");
            }
            return bigDecimalPrice;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Price entry [" + price + "] is invalid");
        }
    }
}
