package com.jobis.tax.components.incom.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//객체의 독립성을 위해 UserReq 클래스를 상속 받지않고 생성
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scrap {
    private String apiKey;
    private String name;
    private String regNo;
    private String userId;

}
