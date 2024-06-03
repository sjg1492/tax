package com.jobis.tax.components.incom;

import com.jobis.tax.common.dto.req.JwtTokenRequestHeader;
import com.jobis.tax.common.util.JwtUtil;
import com.jobis.tax.components.incom.dto.model.Scrap;
import com.jobis.tax.components.incom.dto.req.ScrapRequestHeader;
import com.jobis.tax.components.incom.dto.res.RefundResponse;
import com.jobis.tax.components.incom.svc.IncomService;
import com.jobis.tax.components.user.dto.res.LoginRes;
import com.jobis.tax.components.user.entity.User;
import com.jobis.tax.components.user.svc.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/szs")
public class IncomController {
    private final JwtUtil jwtUtil;
    private final IncomService incomService;
    private final UserService userService;
    @Value("${scrap.api.key}")
    private String apiKey;
    @PostMapping("/scrap")
    public ResponseEntity<?> scrap(@RequestHeader String Authorization){

                //jwtTokenRequestHeader.getAuthorization();
//        String apiKeyHeader = scrapRequestHeader.getX_API_KEY(); //aXC8zK6puHIf9l53L8TiQg==

        if (Authorization == null || !Authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }

        String token = Authorization.substring(7); // "Bearer " 이후의 실제 토큰 값 추출
        String userId = jwtUtil.getUserId(token);
        User user = userService.getUserByUserId(userId);
        incomService.scrapeUserInfo(Scrap.builder()
                        .apiKey(apiKey)
                .name(user.getName())
                .regNo(user.getRegNo())
                .userId(userId)
                .build());

        return ResponseEntity.ok("success");

    }

    @GetMapping("/refund")
    public ResponseEntity<?> refund(@RequestHeader String Authorization){
        WebClient webClient = WebClient.builder()
                .baseUrl("http://kobis.or.kr/kobisopenapi/webservice/rest/movie")
                .build();

        String result = webClient.get()
                .uri("/searchMovieList.json?key=f5eef3421c602c6cb7ea224104795888")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(result);

//        String token = Authorization.substring(7); // "Bearer " 이후의 실제 토큰 값 추출
//        String userId = jwtUtil.getUserId(token);
//
        return ResponseEntity.ok(result);
    }

}
