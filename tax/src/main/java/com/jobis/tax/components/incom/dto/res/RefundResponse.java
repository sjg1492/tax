package com.jobis.tax.components.incom.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {
    @JsonProperty("결정세액")
    private String decisionTaxAmount;
}
