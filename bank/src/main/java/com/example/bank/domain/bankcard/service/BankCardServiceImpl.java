package com.example.bank.domain.bankcard.service;


import com.example.bank.domain.accounter.entity.Accounter;
import com.example.bank.domain.accounter.repository.AccounterRepository;
import com.example.bank.domain.bankcard.dto.*;
import com.example.bank.domain.bankcard.entity.BankCard;
import com.example.bank.domain.bankcard.entity.BankCardPayment;
import com.example.bank.domain.bankcard.repository.BankCardPaymentRepository;
import com.example.bank.domain.bankcard.repository.BankCardRepository;
import com.example.bank.global.exception.BankException;
import com.example.bank.global.firebase.service.FCMService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class BankCardServiceImpl implements BankCardService{


    @Autowired
    private AccounterRepository accounterRepository;
    @Autowired
    private BankCardRepository bankCardRepository;
    @Autowired
    private BankCardPaymentRepository bankCardPaymentRepository;

    @Autowired
    private FCMService fcmService;

    @Override
    @Cacheable(value = "cardLists", key="#cardListRequestDto.privateId")
    public List<CardListResponseDto> getCardList(CardListRequestDto cardListRequestDto) {
        Optional<Accounter> accounterOpt = accounterRepository.findByPrivateId(cardListRequestDto.getPrivateId());

        if (!accounterOpt.isPresent()) {
            throw new EntityNotFoundException("사용자가 존재하지 않습니다.");
        }

        Accounter accounter = accounterOpt.get();
        List<BankCard> bankCardList = accounter.getBankCardList();

        if (bankCardList == null || bankCardList.isEmpty()) {
            return new ArrayList<>();
        }

        return bankCardList.stream()
                .filter(Objects::nonNull)
                .map(card -> new CardListResponseDto(
                        card.getCardNumber(),
                        card.getCardPassword(),
                        card.getCvc(),
                        card.getCardCoName().getCardCoName(),
                        card.getCardCoName().getIdx(),
                        card.getCardCoName().getCardCoCode()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "cardPaymentLists", key = "#cardPaymentDetailRequestDto.privateId")
    public List<CardPaymentDetailResponseDto> getCardPaymentList(CardPaymentDetailRequestDto cardPaymentDetailRequestDto) {

        List<BankCardPayment> bankCardPaymentList = bankCardPaymentRepository.findByBankCard_Accounter_PrivateIdAndPaymentAtBetween(
                cardPaymentDetailRequestDto.getPrivateId(),
                cardPaymentDetailRequestDto.getStartDate(),
                cardPaymentDetailRequestDto.getEndDate()
        );
        return bankCardPaymentList.stream().map(payment ->
                new CardPaymentDetailResponseDto(
                        payment.getPaymentAt(),
                        payment.getPaymentAmount(),
                        payment.getPaymentContent(),
                        payment.getApprovalNumber(),
                        payment.getStoreAddress(),
                        payment.getStoreSector()
                )
        ).collect(Collectors.toList());
    }

    @Override
        public CardPaymentPushResponseDto getCardPaymentAlert(CardPaymentPushRequestDto cardPaymentPushRequestDto){
            Optional<BankCard> bankCardOptional = bankCardRepository.findByCardNumberAndCvc(cardPaymentPushRequestDto.getCardNumber(), cardPaymentPushRequestDto.getCvc());

            if (!bankCardOptional.isPresent()){
                throw new EntityNotFoundException("카드정보가 잘못되었거나 존재하지 않습니다.");
            }

            Accounter accounter = bankCardOptional.get().getAccounter();
            String memberPrivateId = accounter.getPrivateId();
            LocalDateTime time = LocalDateTime.now();
            time = time.plusHours(9);

            String storeSector;
            List<String> categories = new ArrayList<>(Arrays.asList(
                "식비", "커피와 디저트", "주류", "마트", "편의점", "교통/자동차", "숙소", "쇼핑", "레저"));

            if (categories.contains(cardPaymentPushRequestDto.getStoreSector())) {
                storeSector = cardPaymentPushRequestDto.getStoreSector();
            } else {
                storeSector = "그 외";
            }


            BankCardPayment bankCardPayment = BankCardPayment.builder()
                    .paymentAt(time)
                    .paymentAmount(cardPaymentPushRequestDto.getPaymentAmount())
                    .paymentContent(cardPaymentPushRequestDto.getPaymentContent())
                    .storeAddress(cardPaymentPushRequestDto.getStoreAddress())
                    .storeSector(storeSector)
                    .bankCard(bankCardOptional.get())
                    .build();

            bankCardPaymentRepository.save(bankCardPayment);

            //캐싱삭제
//            evictCardDetailsCache(memberPrivateId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedPaymentAt = bankCardPayment.getPaymentAt().format(formatter);
            CardPaymentPushResponseDto responseDto = new CardPaymentPushResponseDto(
                    memberPrivateId,
                    formattedPaymentAt,
                    bankCardPayment.getPaymentAmount(),
                    bankCardPayment.getPaymentContent(),
                    bankCardPayment.getStoreSector(),
                    bankCardPayment.getStoreAddress()
            );


        // fcm 토큰 가져오기
        String fcm = accounter.getFcmToken();

        if(fcm.isEmpty()){
            throw new BankException("fcm값이 없습니다");
        }
        else{
            fcmService.cardFcm(fcm, bankCardPayment, cardPaymentPushRequestDto.getCardNumber());
            return responseDto;

        }


    }
    @CacheEvict(value = "cardPaymentLists", key = "#privateId")
    public void evictCardDetailsCache(String privateId) {
        // 이 메서드는 CacheEvict 어노테이션을 사용하여 캐시를 비우기 위한 용도로만 사용됩니다.
    }


}
