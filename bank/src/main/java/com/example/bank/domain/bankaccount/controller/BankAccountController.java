package com.example.bank.domain.bankaccount.controller;


import com.example.bank.domain.bankaccount.dto.TransferRequestDto;
import com.example.bank.domain.bankaccount.dto.request.BalanceRequestDto;
import com.example.bank.domain.bankaccount.dto.response.DetailResponseDto;
import com.example.bank.domain.bankaccount.dto.response.*;
import com.example.bank.domain.bankaccount.dto.request.BankAccountRequestDto;
import com.example.bank.domain.bankaccount.dto.request.WonRequestDto;
import com.example.bank.domain.bankaccount.dto.request.OneRequestDto;
import com.example.bank.domain.bankaccount.entity.BankName;
import com.example.bank.domain.bankaccount.repository.BankNameRepository;
import com.example.bank.domain.bankaccount.service.BankAccountService;
import com.example.bank.global.exception.BankException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/bank/account")
public class BankAccountController {


    @Autowired
    private final BankAccountService bankAccountService;

    @Autowired
    private BankNameRepository bankNameRepository;


    @Operation(summary = "1원이체 요청", description = "요청 시 사용자의 계좌로 1원 및 랜덤으로 생성된 6자리 숫자를 보냅니다." +
            "name : 사용자이름, bankname: 은행명, account : 계좌번호")
    @PostMapping(value = "/1request")
    public ResponseEntity<OneResponseDto> oneTransferMoney(@RequestBody OneRequestDto oneRequestDto) {
        OneResponseDto responseDto = bankAccountService.oneTransferMoney(oneRequestDto).getBody();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @Operation(summary = "1원이체 코드 확인", description = "이체 코드를 확인한 후, 사용자의 계좌 및 카드 정보를 반환합니다.")
    @PostMapping(value = "/1response")
    public ResponseEntity<WonResponseDto> oneCheckMoney(@RequestBody WonRequestDto wonRequestDto){
        WonResponseDto wonResponseDto = bankAccountService.oneCheckMoney(wonRequestDto);

        return new ResponseEntity<>(wonResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "전체계좌 확인", description = "사용자의 계좌 정보를 반환합니다.")
    @PostMapping(value = "/search/account")
    public ResponseEntity<List<BankAccountResponseDto>> searchAccount(@RequestBody BankAccountRequestDto bankAccountRequestDto) {
        List<BankAccountResponseDto> bankAccountResponseDtos = bankAccountService.searchAccount(bankAccountRequestDto);

        return new ResponseEntity<>(bankAccountResponseDtos, HttpStatus.OK);
    }

    @Operation(summary = "계좌 잔액 조회", description = "특정 계좌의 잔액을 조회합니다.")
    @PostMapping(value = "/search/balance")
    public ResponseEntity<BalanceResponseDto> searchBalance(@RequestBody BalanceRequestDto balanceRequestDto) {
        try {
            BalanceResponseDto balanceResponseDto = bankAccountService.searchBalance(balanceRequestDto);
            return new ResponseEntity<>(balanceResponseDto, HttpStatus.OK);
        } catch (BankException e) {
            // 예외 처리: 사용자나 계좌가 유효하지 않은 경우
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "전체 은행사 코드 확인", description = "은행사의 모든 코드를 반환합니다.")
    @PostMapping(value = "/search/bankCode")
    public List<BankCodeResponseDto> searchBankCode(){
        List<BankName> bankNames = bankNameRepository.findAll();

        return bankNames.stream()
                .map(bankName -> new BankCodeResponseDto(bankName.getBankName(), bankName.getIdx(), bankName.getBankCode()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "특정 계좌 거래내역 조회", description = "은행사의 모든 코드를 반환합니다.")
    @PostMapping(value = "/search/account/detail")
    public ResponseEntity<List<DetailResponseDto>> searchAccountDetail(@RequestBody BalanceRequestDto balanceRequestDto) {
        List<DetailResponseDto> detailResponseDtoList = bankAccountService.searchAccountDetail(balanceRequestDto);
        return new ResponseEntity<>(detailResponseDtoList, HttpStatus.OK);
    }

    @Operation(summary = "계좌이체", description = "요청시 계좌이체가 실행됩니다.")
    @PostMapping(value = "/transfer")
    public ResponseEntity<String> transferAccount(@RequestBody TransferRequestDto transferRequestDto){
        try {
            String response = bankAccountService.transferAccount(transferRequestDto, "계좌");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BankException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}