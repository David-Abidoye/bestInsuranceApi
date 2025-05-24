package com.bestinsurance.api.helper;

public class ConstraintHelper {

    private ConstraintHelper() {
    }

    public static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String DATE_REGEX = "^[1-2]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\\d|3[0-1])$";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";
}
