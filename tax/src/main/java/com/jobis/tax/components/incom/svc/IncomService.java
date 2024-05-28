package com.jobis.tax.components.incom.svc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobis.tax.components.incom.dto.model.Scrap;
import com.jobis.tax.components.incom.dto.model.ScrapResponse;
import com.jobis.tax.components.incom.entity.UserIncomeDeduction;
import com.jobis.tax.components.incom.entity.UserTaxInformation;
import com.jobis.tax.components.incom.repository.UserIncomeDeductionRepository;
import com.jobis.tax.components.incom.repository.UserTaxInformationRepository;
import com.jobis.tax.components.user.repository.UserRepository;
import com.jobis.tax.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import com.jobis.tax.common.error.WebClientException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


@RequiredArgsConstructor
@Service
public class IncomService {


    private final WebClientConfig webClientConfig;
    private final ObjectMapper objectMapper;
    private final UserIncomeDeductionRepository userIncomeDeductionRepository;
    private final UserTaxInformationRepository userTaxInformationRepository;

    @Async
    @Transactional
    public Future<Void> scrapeUserInfo(Scrap scrap) {
        try {
            ScrapResponse scrapResponse = callWebClient(scrap);
            String scrapResponseTos = scrapResponse.toString();

            List<UserIncomeDeduction> deductions = new ArrayList<>();
            BigDecimal incomeDeduction = BigDecimal.ZERO;

            // 국민연금 소득공제 저장
            for (ScrapResponse.Pension pension : scrapResponse.getData().getDeductions().getPension()) {
                UserIncomeDeduction deduction = new UserIncomeDeduction();
                deduction.setUserId(scrap.getUserId());
                deduction.setDeductionType("국민연금");
                deduction.setDeductionMonth(pension.getMonth());
                BigDecimal deductibleAmount = scrapResponse.convertStringToBigDecimal(pension.getAmount());
                deduction.setDeductible(deductibleAmount);
                incomeDeduction = incomeDeduction.add(deductibleAmount);
                deductions.add(deduction);
            }

            // 신용카드 소득공제 저장
            int year = scrapResponse.getData().getDeductions().getCreditCardDeduction().getYear();
            List<Map<String, String>> monthList = scrapResponse.getData().getDeductions().getCreditCardDeduction().getMonth();
            for (Map<String, String> monthMap : monthList) {
                for (Map.Entry<String, String> entry : monthMap.entrySet()) {
                    UserIncomeDeduction deduction = new UserIncomeDeduction();
                    deduction.setUserId(scrap.getUserId());
                    deduction.setDeductionType("신용카드소득공제");
                    deduction.setDeductionMonth(year + "-" + entry.getKey());
                    BigDecimal deductibleAmount = scrapResponse.convertStringToBigDecimal(entry.getValue());
                    deduction.setDeductible(deductibleAmount);
                    incomeDeduction = incomeDeduction.add(deductibleAmount);
                    deductions.add(deduction);
                }
            }

            // 세액공제 저장
            String taxCredit = scrapResponse.getData().getDeductions().getTaxCredit();
            UserIncomeDeduction taxCreditDeduction = new UserIncomeDeduction();
            taxCreditDeduction.setUserId(scrap.getUserId());
            taxCreditDeduction.setDeductionType("세액공제");
            taxCreditDeduction.setDeductionMonth(year + "-01");
            BigDecimal taxCreditAmount = scrapResponse.convertStringToBigDecimal(taxCredit);
            taxCreditDeduction.setDeductible(taxCreditAmount);
            deductions.add(taxCreditDeduction);

            // 모든 소득 공제 항목을 한 번에 저장
            userIncomeDeductionRepository.saveAll(deductions);

            // 사용자 세금 정보 저장
            UserTaxInformation userIncomeDeduction = new UserTaxInformation();

            userIncomeDeduction.setUserId(scrap.getUserId());
            userIncomeDeduction.setName(scrapResponse.getData().getName());
            userIncomeDeduction.setTotalIncome(scrapResponse.convertStringToBigDecimal(scrapResponse.getData().getTotalIncome()));
            userIncomeDeduction.setIncomeDeduction(incomeDeduction);
            userIncomeDeduction.setTaxCredit(taxCreditAmount);
            userTaxInformationRepository.save(userIncomeDeduction);

            return CompletableFuture.completedFuture(null);
        } catch (WebClientException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    public ScrapResponse callWebClient(Scrap scrap){
        try {
            String response = webClientConfig.webClient().post()
                    .uri("/scrap")
                    .header("X-API-KEY", scrap.getApiKey())
                    .header("Content-Type", "application/json")
                    .bodyValue("{\"name\" : \"" + scrap.getName() + "\", \"regNo\" : \"" + scrap.getRegNo() + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // 블로킹 호출로 응답을 기다림

            return objectMapper.readValue(response, ScrapResponse.class);

        } catch (WebClientResponseException e) {
            // 에러 처리
            e.printStackTrace();
            throw new WebClientException("WebClient response error", e);

        } catch (Exception e) {
            e.printStackTrace();
            throw new WebClientException("General error", e);
        }

    }

    public String getUserDeterminedTaxAmount(String userId){
        UserTaxInformation  userTaxInformation = userTaxInformationRepository.findByUserId(userId);
        //과세표준
        BigDecimal taxBase = userTaxInformation.getTotalIncome().subtract(userTaxInformation.getTaxCredit());

        //산출 세액
        BigDecimal calculatedTaxAmount = calculateBasicTax(taxBase);

        //결정 세액
        BigDecimal determinedTaxAmount =calculatedTaxAmount.subtract(userTaxInformation.getTaxCredit());

        return formatTaxAmount(determinedTaxAmount);
    }
    private  String formatTaxAmount(BigDecimal taxAmount) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(taxAmount);
    }
    private BigDecimal calculateBasicTax(BigDecimal taxBase) {
        if (taxBase.compareTo(new BigDecimal("14000000")) <= 0) {
            return taxBase.multiply(new BigDecimal("0.06"));
        } else if (taxBase.compareTo(new BigDecimal("50000000")) <= 0) {
            BigDecimal excessAmount = taxBase.subtract(new BigDecimal("14000000"));
            return new BigDecimal("840000").add(excessAmount.multiply(new BigDecimal("0.15")));
        } else if (taxBase.compareTo(new BigDecimal("88000000")) <= 0) {
            BigDecimal excessAmount = taxBase.subtract(new BigDecimal("50000000"));
            return new BigDecimal("6240000").add(excessAmount.multiply(new BigDecimal("0.24")));
        } else if (taxBase.compareTo(new BigDecimal("150000000")) <= 0) {
            BigDecimal excessAmount = taxBase.subtract(new BigDecimal("88000000"));
            return new BigDecimal("15360000").add(excessAmount.multiply(new BigDecimal("0.35")));
        } else if (taxBase.compareTo(new BigDecimal("300000000")) <= 0) {
            BigDecimal excessAmount = taxBase.subtract(new BigDecimal("150000000"));
            return new BigDecimal("37060000").add(excessAmount.multiply(new BigDecimal("0.38")));
        } else if (taxBase.compareTo(new BigDecimal("500000000")) <= 0) {
            BigDecimal excessAmount = taxBase.subtract(new BigDecimal("300000000"));
            return new BigDecimal("94060000").add(excessAmount.multiply(new BigDecimal("0.40")));
        } else if (taxBase.compareTo(new BigDecimal("1000000000")) <= 0) {
            BigDecimal excessAmount = taxBase.subtract(new BigDecimal("500000000"));
            return new BigDecimal("174060000").add(excessAmount.multiply(new BigDecimal("0.42")));
        } else {
            BigDecimal excessAmount = taxBase.subtract(new BigDecimal("1000000000"));
            return new BigDecimal("384060000").add(excessAmount.multiply(new BigDecimal("0.45")));
        }
    }
}
