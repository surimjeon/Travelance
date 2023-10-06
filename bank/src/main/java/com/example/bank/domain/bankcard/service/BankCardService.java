package com.example.bank.domain.bankcard.service;

import com.example.bank.domain.bankcard.dto.*;

import java.util.List;

public interface BankCardService {


    // 사용자 카드 리스트 조회
    List<CardListResponseDto> getCardList(CardListRequestDto cardListRequestDto) throws IllegalAccessException;

    // 사용자 카드 사용내역 조회
    List<CardPaymentDetailResponseDto> getCardPaymentList(CardPaymentDetailRequestDto cardPaymentDetailRequestDto);

    // 결제내역 저장
    CardPaymentPushResponseDto getCardPaymentAlert(CardPaymentPushRequestDto cardPaymentPushRequestDto);

}
