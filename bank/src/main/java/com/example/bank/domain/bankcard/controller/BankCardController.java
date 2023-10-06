package com.example.bank.domain.bankcard.controller;


import com.example.bank.domain.bankcard.dto.*;
import com.example.bank.domain.bankcard.service.BankCardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank/card")
public class BankCardController {


    private final BankCardService bankCardService;

    @Autowired
    private KafkaTemplate<String, CardPaymentPushResponseDto> kafkaTemplate;

    @PostMapping("/list")
    @Operation(summary = "사용자 카드 조회")
    public ResponseEntity<List<CardListResponseDto>> getCardList(@RequestBody CardListRequestDto cardListRequestDto) throws IllegalAccessException {
        List<CardListResponseDto> cardList = bankCardService.getCardList(cardListRequestDto);
        return new ResponseEntity<>(cardList, HttpStatus.OK);
    }

    @PostMapping("/payment")
    @Operation(summary = "카드 결제 내역 조회", description = "사용자 카드 결제 내역을 조회합니다.")
    public ResponseEntity<List<CardPaymentDetailResponseDto>> getCardPaymentList(@RequestBody CardPaymentDetailRequestDto cardPaymentDetailRequestDto) {
        List<CardPaymentDetailResponseDto> cardPaymentList = bankCardService.getCardPaymentList(cardPaymentDetailRequestDto);
        return new ResponseEntity<>(cardPaymentList, HttpStatus.OK);
    }

    @PostMapping("/payment/alert")
    @Operation(summary = "카드 결제 알림", description = "단말기로부터 카드 결제를 수신하여 은행 DB에 저장한 후, 비즈니스 서버로 결제 알림을 보냅니다.")
    public ResponseEntity<?> getPaymentAlert(@RequestBody CardPaymentPushRequestDto cardPaymentPushRequestDto){
        CardPaymentPushResponseDto response = bankCardService.getCardPaymentAlert(cardPaymentPushRequestDto);
        // Kafka로 메시지 보내기
        this.kafkaTemplate.send("travelance", response);
        // 성공 메시지를 담은 ResultDto 객체 생성
        ResultDto resultDto = new ResultDto("결제 성공");
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}

