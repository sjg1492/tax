package com.jobis.tax.components.user.dto.req;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpReq extends UserReq {
    @NotNull
    private String name;
    @NotNull
    private String regNo;
}
