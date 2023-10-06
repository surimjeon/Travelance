package com.example.bank.domain.accounter.controller;

import com.example.bank.domain.accounter.dto.FcmRequestDto;
import com.example.bank.domain.accounter.entity.Accounter;
import com.example.bank.domain.accounter.repository.AccounterRepository;
import com.example.bank.domain.common.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/bank/accounter")
public class MemberController {

    private final AccounterRepository accounterRepository;


    @PostMapping("/fcm")
    @Operation(summary = "FCM Token을 저장하는 api")
    public ResponseEntity<ResultDto> saveFcmToken(@RequestBody FcmRequestDto fcmRequestDto) {
        String fcmToken = fcmRequestDto.getFcmToken();
        String name = fcmRequestDto.getName();

        Accounter accounter = accounterRepository.findByName(name);

        if (accounter != null) {
            accounter.setFcmToken(fcmToken);
            accounterRepository.save(accounter);
            return ResponseEntity.ok(new ResultDto("FCM Token 갱신 완료"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResultDto("Accounter를 찾을 수 없습니다."));
        }
    }
}