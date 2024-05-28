package com.jobis.tax.components.incom.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jobis.tax.common.repository.CustomBigDecimalDeserializer;
import com.jobis.tax.common.repository.CustomCreditCardDeductionDeserializer;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Data
@ToString
public class ScrapResponse {
    private String status;
    private Data data;
    private Errors errors;
    public  BigDecimal convertStringToBigDecimal(String value) {
        if (value == null || value.isEmpty()) {
            return BigDecimal.ZERO; // 혹은 적절한 기본값
        }
        // 콤마 제거
        value = value.replace(",", "");
        return new BigDecimal(value);
    }
    // Getters and setters

    @lombok.Data
    @ToString
    public static class Data {
        @JsonProperty("종합소득금액")
        private String totalIncome;
        @JsonProperty("이름")
        private String name;
        @JsonProperty("소득공제")
        private Deductions deductions;
        // Getters and setters
    }

    @lombok.Data
    @ToString
    public static class Deductions {
        @JsonProperty("국민연금")
        private List<Pension> pension;
        @JsonProperty("신용카드소득공제")
        private CreditCardDeduction creditCardDeduction;
        @JsonProperty("세액공제")
        private String taxCredit;
    }

    @lombok.Data
    @ToString
    public static class Pension {
        @JsonProperty("월")
        private String month;
        @JsonProperty("공제액")
        private String amount;

    }

    @lombok.Data
    @ToString
    public static class CreditCardDeduction {
        private int year;
        private List<Map<String, String>> month;

    }

    @lombok.Data
    @ToString
    public static class Errors {
        private String code;
        private String message;
        private String validations;

    }

}
