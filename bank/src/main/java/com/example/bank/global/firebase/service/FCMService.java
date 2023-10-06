package com.example.bank.global.firebase.service;


import com.example.bank.domain.bankaccount.entity.BankAccount;
import com.example.bank.domain.bankaccount.entity.BankAccountTransfer;
import com.example.bank.domain.bankaccount.repository.BankAccountRepository;
import com.example.bank.domain.bankcard.entity.BankCardPayment;
import com.example.bank.domain.bankcard.entity.CardCoName;
import com.example.bank.domain.bankcard.repository.BankCardRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {

    @Autowired
    private final BankAccountRepository bankAccountRepository;



    public void sendFCMNotification(String token, String title, String body) {
        try{

            Map<String, String> data = new HashMap<>();

            // 메시지
            data.put("title", title);
            data.put("message", body);

            // 알림 보낼 대상 설정
            Message message = Message.builder()
                    .putAllData(data)
                    .setToken(token)
                    .build();

            // FCM으로 알림 전송
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    // 1원 요청
    public void oneRequest(String fcmToken, BankAccountTransfer bankAccountTransfer){

        String num = bankAccountTransfer.getWithdrawalNumber();
        BankAccount bankAccount = bankAccountRepository.findBankAccountByAccount(num);
        String name = bankAccount.getAccounter().getName();
        // 타이틀 : 입금인지 출금인지, 금액
        String title =String.format("입금 %s원",bankAccountTransfer.getAmount());

        // 이름(*) 님 , 날짜, 내 계좌번호 **로 표시, 보낸 사람 이름, 메모, 금액, 잔액: 잔액
        String depositNumber = bankAccountTransfer.getDepositNumber().substring(0, 4) + "************";
        String body = String.format("%s님 %s %s %s %s원 잔액 %s원",
                bankAccountTransfer.getBankAccount().getAccounter().getName(),
                depositNumber, name,
                bankAccountTransfer.getMemo(),bankAccountTransfer.getAmount(), bankAccountTransfer.getBankAccount().getBalance());

        log.info("body : " + body);
        log.info("title : " + title);
        this.sendFCMNotification(fcmToken,title,body);

    }

    public void out(String fcmToken, BankAccountTransfer bankAccountTransfer){

        // 상대방 이름
        String num = bankAccountTransfer.getWithdrawalNumber();
        BankAccount bankAccount = bankAccountRepository.findBankAccountByAccount(num);
        String name = bankAccount.getAccounter().getName();

        Long balance = bankAccount.getBalance();

        // 타이틀 : 입금인지 출금인지, 금액
        String title =String.format("출금 %s원",bankAccountTransfer.getAmount());

        // 이름(*) 님 , 날짜, 내 계좌번호 **로 표시, 보낸 사람 이름, 메모, 금액, 잔액: 잔액
        String depositNumber = num.substring(0, 4) + "************";

        String body = String.format("%s님 %s %s %s %s원 잔액 %s원",
                name,
                depositNumber, bankAccountTransfer.getBankAccount().getAccounter().getName() ,
                bankAccountTransfer.getMemo(),bankAccountTransfer.getAmount(), balance);

        log.info("body : " + body);
        log.info("title : " + title);
        this.sendFCMNotification(fcmToken,title,body);

    }


    public void oneRequest2(String fcmToken, BankAccountTransfer bankAccountTransfer){

        String num = bankAccountTransfer.getWithdrawalNumber();
        BankAccount bankAccount = bankAccountRepository.findBankAccountByAccount(num);
        String name = bankAccount.getAccounter().getName();

        Long balance = bankAccount.getBalance();

        // 타이틀 : 입금인지 출금인지, 금액
        String title =String.format("입금 %s원",bankAccountTransfer.getAmount());


        // 이름(*) 님 , 날짜, 내 계좌번호 **로 표시, 보낸 사람 이름, 메모, 금액, 잔액: 잔액
        String depositNumber = num.substring(0, 4) + "************";

        String body = String.format("%s님 %s %s %s %s원 잔액 %s원",
                name ,
                depositNumber, bankAccountTransfer.getBankAccount().getAccounter().getName(),
                bankAccountTransfer.getMemo(),bankAccountTransfer.getAmount(), balance);

        log.info("body : " + body);
        log.info("title : " + title);
        this.sendFCMNotification(fcmToken,title,body);

    }

    public void out2(String fcmToken, BankAccountTransfer bankAccountTransfer){

        // 상대방 이름
        String num = bankAccountTransfer.getWithdrawalNumber();
        BankAccount bankAccount = bankAccountRepository.findBankAccountByAccount(num);
        String name = bankAccount.getAccounter().getName();

        Long balance = bankAccount.getBalance();

        // 타이틀 : 입금인지 출금인지, 금액
        String title =String.format("출금 %s원",bankAccountTransfer.getAmount());

        // 이름(*) 님 , 날짜, 내 계좌번호 **로 표시, 보낸 사람 이름, 메모, 금액, 잔액: 잔액
        String depositNumber = bankAccountTransfer.getDepositNumber().substring(0, 4) + "************";
        String body = String.format("%s님 %s %s %s %s원 잔액 %s원",
                bankAccountTransfer.getBankAccount().getAccounter().getName(),
                depositNumber,name ,
                bankAccountTransfer.getMemo(),bankAccountTransfer.getAmount(), bankAccountTransfer.getBankAccount().getBalance());

        log.info("body : " + body);
        log.info("title : " + title);
        this.sendFCMNotification(fcmToken,title,body);

    }


    // 카드 결재 내역 알림
    public void cardFcm(String fcmToken, BankCardPayment bankCardPayment, String cardNumber){

        Long paymentAmount = bankCardPayment.getPaymentAmount();
        String paymentContent = bankCardPayment.getPaymentContent();
        String storeAddress = bankCardPayment.getStoreAddress();
        CardCoName cardCoName = bankCardPayment.getBankCard().getCardCoName();
        LocalDateTime time = bankCardPayment.getPaymentAt();

        time = time.plusHours(9);

        String cardName = cardCoName.getCardCoName();
        String title = String.format("%s카드 승인", cardName);
        String body = String.format("%s원 %s %s %s %s:%s", paymentAmount, paymentContent, storeAddress,time.toLocalDate().toString(),time.getHour(),time.getMinute());

        log.info("body : " + body);
        log.info("title : " + title);

        this.sendFCMNotification(fcmToken,title,body);
    }

}
