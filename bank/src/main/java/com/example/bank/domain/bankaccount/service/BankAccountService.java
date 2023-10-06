package com.example.bank.domain.bankaccount.service;


import com.example.bank.domain.accounter.entity.Accounter;
import com.example.bank.domain.accounter.repository.AccounterRepository;
import com.example.bank.domain.bankaccount.dto.TransferRequestDto;
import com.example.bank.domain.bankaccount.dto.request.BalanceRequestDto;
import com.example.bank.domain.bankaccount.dto.response.DetailResponseDto;
import com.example.bank.domain.bankaccount.dto.response.BalanceResponseDto;
import com.example.bank.domain.bankaccount.dto.request.BankAccountRequestDto;
import com.example.bank.domain.bankaccount.dto.response.BankAccountResponseDto;
import com.example.bank.domain.bankaccount.dto.request.WonRequestDto;
import com.example.bank.domain.bankaccount.dto.response.WonResponseDto;
import com.example.bank.domain.bankaccount.dto.request.OneRequestDto;
import com.example.bank.domain.bankaccount.dto.response.OneResponseDto;
import com.example.bank.domain.bankaccount.entity.BankAccount;
import com.example.bank.domain.bankaccount.entity.BankAccountTransfer;
import com.example.bank.domain.bankaccount.repository.BankAccountRepository;
import com.example.bank.domain.bankaccount.repository.BankAccountTransferRepository;
import com.example.bank.global.exception.BankException;
import com.example.bank.global.firebase.service.FCMService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private AccounterRepository accounterRepository;

    @Autowired
    private BankAccountTransferRepository bankAccountTransferRepository;

    @Autowired
    private FCMService fcmService;


    @CacheEvict(value = "accountDetails", key = "#oneRequestDto.account")
    public ResponseEntity<OneResponseDto> oneTransferMoney(OneRequestDto oneRequestDto) {
        String name = oneRequestDto.getName();
        String bankName = oneRequestDto.getBankName();
        String accountNumber = oneRequestDto.getAccount();

        log.info("accountNumber : " + accountNumber);

        try {
            // 해당 계좌 정보 가져오기
            BankAccount bankAccount = bankAccountRepository.findByAccount(accountNumber)
                    .orElseThrow(() -> new BankException("계좌를 찾을 수 없습니다."));

            // 계좌 주인이 name과 같은지 확인
            if (!bankAccount.getAccounter().getName().equals(name)) {
                throw new BankException("계좌 주인이 일치하지 않습니다.");
            }

            // 계좌의 은행명이 bankName과 같은지 확인
            if (!bankAccount.getBankName().getBankName().equals(bankName)) {
                throw new BankException("계좌의 은행명이 일치하지 않습니다.");
            }

            // 랜덤 난수 4자리 생성
            String randomNumber = generateRandomNumber();

            String accounterName = "은행";
            Accounter accounter = accounterRepository.findByName(accounterName);
            BankAccount bankAccount1 = bankAccountRepository.findByAccounter(accounter);
            String bankAccountNumber = bankAccount1.getAccount();

            LocalDateTime time = LocalDateTime.now();
            time = time.plusHours(9);

            log.warn("bankAccountNumber : " + bankAccountNumber);
            // 결제 내역 저장
            TransferRequestDto transferRequestDto = new TransferRequestDto(bankAccount.getAccount(),1L,randomNumber,time,bankAccountNumber);


            String transferResult = transferAccount(transferRequestDto,"1원");
            if (!transferResult.equals("이체 성공")) {
                throw new BankException("이체에 실패하였습니다.");
            }
            else{
                return new ResponseEntity<>(new OneResponseDto(randomNumber), HttpStatus.OK);
            }
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OneResponseDto(e.getMessage()));
        }
    }
    
    // 1원 확인
    public WonResponseDto oneCheckMoney (WonRequestDto wonRequestDto) {
        // 받은 사람의 계좌번호
        String account = wonRequestDto.getAccount();
        log.warn("account : " + account);
        // 은행이라는 사람의 계좌 번호
        Accounter accounter = accounterRepository.findByName("은행");
        BankAccount bankAccount1 = bankAccountRepository.findByAccounter(accounter);
        String bankAccountNumber = bankAccount1.getAccount();
        log.warn("bankAccountNumber : " + bankAccountNumber);

        List<String> vList = bankAccountTransferRepository.findLatestMemoByDepositNumberAndWithdrawalNumber(account, bankAccountNumber);
        log.warn("vList : " + vList);
        if (!vList.isEmpty()) {
            String latestMemo = vList.get(0); // 첫 번째 결과를 사용합니다
            log.warn("latestTransfer : " + latestMemo);

            String verifyCode = wonRequestDto.getVerifyCode();
            log.warn("verifyCode : " + verifyCode);
            if (latestMemo.equals(verifyCode)){
                String privateId = bankAccountRepository.findPrivateIdByAccountNumber(account);
                log.warn("privateId : " + privateId);
                return new WonResponseDto(privateId);
            } else {
                throw new BankException("일치하지 않습니다");
            }
        } else {
            throw new BankException("결과가 없습니다");
        }
    }

    // 랜덤 난수 4자리
    private String generateRandomNumber() {
        Random random = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomDigit = i == 0 ? random.nextInt(9) + 1 : random.nextInt(10);
            randomNumber.append(randomDigit);
        }
        return randomNumber.toString();
}
    // 전체 계좌 조회
    @Cacheable(value = "bankAccounts", key = "#bankAccountRequestDto.privateId")
    public List<BankAccountResponseDto> searchAccount(BankAccountRequestDto bankAccountRequestDto) {
        String privateId = bankAccountRequestDto.getPrivateId();

        log.info("privateId : " + privateId);

        Optional<Accounter> accounterOptional = accounterRepository.findByPrivateId(privateId);

        if (accounterOptional.isPresent()) {
            Accounter accounter = accounterOptional.get();
            Long accounterId = accounter.getId();
            List<BankAccount> bankAccounts = bankAccountRepository.findByAccounterId(accounterId);

            return bankAccounts.stream()
                    .map(bankAccount -> new BankAccountResponseDto(
                            bankAccount.getAccount(),
                            bankAccount.getCreatedAt(),
                            bankAccount.getBalance(),
                            bankAccount.getAccounter().getPrivateId(),
                            bankAccount.getBankName().getBankName(),
                            bankAccount.getBankName().getIdx()
                    ))
                    .collect(Collectors.toList());
        } else {
            throw new NoSuchElementException("해당하는 사용자 정보가 없습니다.");
        }
    }

    // 계좌 내역 조회
    @Cacheable(value = "accountDetails", key = "#balanceRequestDto.account")
    public List<DetailResponseDto> searchAccountDetail(BalanceRequestDto balanceRequestDto) throws BankException {
        String privateId = balanceRequestDto.getPrivateId();
        String account = balanceRequestDto.getAccount();

        // 해당 사용자 가져오기
        Optional<Accounter> accounterOptional = accounterRepository.findByPrivateId(privateId);

        // 사용자 존재하는지
        if (accounterOptional.isPresent()) {
            Accounter accounter = accounterOptional.get();
            // 사용자의 고유번호와 리퀘스트 고유번호가 같은지, 리퀘스트받은 계좌가 DB에서 찾은 사용자 계좌목록에 있는지 체크
            if (privateId.equals(accounter.getPrivateId()) && accounter.getBankAccountList().stream().anyMatch(bankAccount -> bankAccount.getAccount().equals(account))) {
                // 해당 계좌의 입출금 내역 조회
                List<BankAccountTransfer> transferList = bankAccountTransferRepository.findByAccountNumber(account);

                // DetailResponseDto로 변환하면서 입금, 출금 나눠서 저장
                List<DetailResponseDto> detailResponseDtoList = new ArrayList<>();
                for (BankAccountTransfer transfer : transferList) {
                    // 입금
                    if (transfer.getDepositNumber().equals(account)) {
                        detailResponseDtoList.add(new DetailResponseDto(
                                transfer.getDepositNumber(),
                                transfer.getAmount(),
                                transfer.getMemo(),
                                transfer.getTransferAt(),
                                transfer.getWithdrawalNumber(),
                                "입금"
                        ));
                    } else if (transfer.getWithdrawalNumber().equals(account)) {
                        // 출금
                        detailResponseDtoList.add(new DetailResponseDto(
                                transfer.getDepositNumber(),
                                transfer.getAmount(),
                                transfer.getMemo(),
                                transfer.getTransferAt(),
                                transfer.getWithdrawalNumber(),
                                "출금"
                        ));
                    }
                }

                // 리스트를 시간 기준으로 내림차순으로 정렬
                detailResponseDtoList.sort(Comparator.comparing(DetailResponseDto::getTransferAt).reversed());

                return detailResponseDtoList;
            }
        }
        // 만약 위의 조건에 해당하지 않으면, 예외처리
        throw new BankException("Invalid privateId or account");
    }

    // 잔액 조회
    public BalanceResponseDto searchBalance (BalanceRequestDto balanceRequestDto) throws BankException {
        String privateId = balanceRequestDto.getPrivateId();
        String account = balanceRequestDto.getAccount();

        // 해당 사용자 가져오기
        Optional<Accounter> accounterOptional = accounterRepository.findByPrivateId(privateId);

        // 사용자 존재하는지
        if (accounterOptional.isPresent()) {
            Accounter accounter = accounterOptional.get();
            // 사용자의 고유번호와 리퀘스트 고유번호가 같은지, 리퀘스트받은 계좌가 DB에서 찾은 사용자 계좌목록에 있는지 체크
            if (privateId.equals(accounter.getPrivateId()) && accounter.getBankAccountList().stream().anyMatch(bankAccount -> bankAccount.getAccount().equals(account))){
                BankAccount bankAccount = bankAccountRepository.findBankAccountByAccount(account);
                Long balance = bankAccount.getBalance();
                return new BalanceResponseDto(account, balance);
            }
        }

        // 만약 위의 조건에 해당하지 않으면, 예외처리
        throw new BankException("Invalid privateId or account");
    }
    
    // 계좌이체
    @Transactional
    public String transferAccount(TransferRequestDto transferRequestDto, String type){
        try {
            String depositNumber = transferRequestDto.getDepositNumber();
            Long amount = transferRequestDto.getAmount();
            String withdrawalNumber = transferRequestDto.getWithdrawalNumber();
            LocalDateTime transferAt = transferRequestDto.getTransferAt();

            transferAt = transferAt.plusHours(9);

            // 계좌 조회
            Optional<BankAccount> depositAccountOptional = bankAccountRepository.findByAccount(depositNumber);
            Optional<BankAccount> withdrawalAccountOptional = bankAccountRepository.findByAccount(withdrawalNumber);

            if (depositAccountOptional.isPresent() && withdrawalAccountOptional.isPresent()) {
                BankAccount depositAccount = depositAccountOptional.get();
                BankAccount withdrawalAccount = withdrawalAccountOptional.get();

                // 출금 계좌에서 잔액 체크
                Long withdrawalBalance = depositAccount.getBalance();
                if (withdrawalBalance < amount) {
                    return "잔액이 부족합니다.";
                }

                // 입금 계좌에 금액 추가
                depositAccount.setBalance(depositAccount.getBalance() + amount);
                
                // 출금 계좌에서 금액 차감
                withdrawalAccount.setBalance(withdrawalAccount.getBalance() - amount);

                // 변경된 잔액을 저장
                bankAccountRepository.save(depositAccount);
                bankAccountRepository.save(withdrawalAccount);


                // 결재 내역 저장
                BankAccountTransfer transferLog = BankAccountTransfer.builder()
                        .transferAt(transferAt)
                        .depositNumber(depositNumber)
                        .withdrawalNumber(withdrawalNumber)
                        .amount(amount)
                        .memo(transferRequestDto.getMemo())
                        .bankAccount(depositAccount)
                        .build();

                bankAccountTransferRepository.save(transferLog);

                if(type.equals("1원")){
                    String fcmMe;
                    // fcm 토큰 가져오기, 당사자
                    Optional<BankAccount> bankAccount1 = bankAccountRepository.findByAccount(depositNumber);
                    if (bankAccount1.isPresent()){
                        fcmMe = bankAccount1.get().getAccounter().getFcmToken();
                        log.info("당사자(출금) : " + fcmMe);
                    }
                    else{
                        fcmMe = accounterRepository.findByName("은행").getFcmToken();
                        log.info("fcm 없음");
                    }

                    String fcmYou;
                    // fcm 토큰 가져오기, 상대방
                    Optional<BankAccount> bankAccount2 = bankAccountRepository.findByAccount(withdrawalNumber);
                    if (bankAccount2.isPresent()){
                        fcmYou = bankAccount2.get().getAccounter().getFcmToken();
                        log.info("상대방(입금) : " + fcmYou);
                    }
                    else{
                        fcmYou = accounterRepository.findByName("은행").getFcmToken();
                        log.info("fcm 없음");
                    }

                    if(!fcmMe.isEmpty()){
                        // 은행이 특정 계좌에 보낸 가장 최신의 결제내역
                        BankAccountTransfer bankAccountTransfer  = bankAccountTransferRepository.findLatestMatchingTransfer(depositNumber, withdrawalNumber);

                        log.info("bankAccountTransfer : " + bankAccountTransfer);
                        fcmService.oneRequest(fcmMe,bankAccountTransfer);

                    }

                    if(!fcmYou.isEmpty()){
                        // 은행이 특정 계좌에 보낸 가장 최신의 결제내역
                        BankAccountTransfer bankAccountTransfer  = bankAccountTransferRepository.findLatestMatchingTransfer(depositNumber, withdrawalNumber);

                        log.info("bankAccountTransfer : " + bankAccountTransfer);
                        fcmService.out(fcmYou,bankAccountTransfer);

                    }
                }
                else{
                    String fcmMe;
                    // fcm 토큰 가져오기, 당사자
                    Optional<BankAccount> bankAccount1 = bankAccountRepository.findByAccount(depositNumber);
                    if (bankAccount1.isPresent()){
                        fcmMe = bankAccount1.get().getAccounter().getFcmToken();
                        log.info("당사자(출금) : " + fcmMe);
                    }
                    else{
                        fcmMe = accounterRepository.findByName("은행").getFcmToken();
                        log.info("fcm 없음");
                    }

                    String fcmYou;
                    // fcm 토큰 가져오기, 상대방
                    Optional<BankAccount> bankAccount2 = bankAccountRepository.findByAccount(withdrawalNumber);
                    if (bankAccount2.isPresent()){
                        fcmYou = bankAccount2.get().getAccounter().getFcmToken();
                        log.info("상대방(입금) : " + fcmYou);
                    }
                    else{
                        fcmYou = accounterRepository.findByName("은행").getFcmToken();
                        log.info("fcm 없음");
                    }

                    if(!fcmMe.isEmpty()){
                        // 은행이 특정 계좌에 보낸 가장 최신의 결제내역
                        BankAccountTransfer bankAccountTransfer  = bankAccountTransferRepository.findLatestMatchingTransfer(depositNumber, withdrawalNumber);

                        log.info("bankAccountTransfer : " + bankAccountTransfer);
                        fcmService.out2(fcmMe,bankAccountTransfer);

                    }

                    if(!fcmYou.isEmpty()){
                        // 은행이 특정 계좌에 보낸 가장 최신의 결제내역
                        BankAccountTransfer bankAccountTransfer  = bankAccountTransferRepository.findLatestMatchingTransfer(depositNumber, withdrawalNumber);

                        log.info("bankAccountTransfer : " + bankAccountTransfer);
                        fcmService.oneRequest2(fcmYou,bankAccountTransfer);

                    }
                }







                // 캐시 비우기
                evictAccountDetailsCache(depositNumber);
                evictAccountDetailsCache(withdrawalNumber);

                return "이체 성공";

            } else {
                return "계좌를 찾지 못했습니다.";
            }
        } catch (Exception e) {
            throw new BankException("Error occurred during transfer: " + e.getMessage());
        }
    }

    @CacheEvict(value = "accountDetails", key = "#accountNumber")
    public void evictAccountDetailsCache(String accountNumber) {
        // 이 메서드는 CacheEvict 어노테이션을 사용하여 캐시를 비우기 위한 용도로만 사용됩니다.
    }

}
