package com.jobis.tax.components.incom.dto.req;

import com.jobis.tax.common.dto.req.JwtTokenRequestHeader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapRequestHeader extends JwtTokenRequestHeader {
    private String X_API_KEY;
}
