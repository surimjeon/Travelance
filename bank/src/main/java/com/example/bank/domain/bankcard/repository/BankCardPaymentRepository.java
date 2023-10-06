package com.example.bank.domain.bankcard.repository;

import com.example.bank.domain.bankcard.entity.BankCardPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankCardPaymentRepository extends JpaRepository<BankCardPayment, Long> {
    List<BankCardPayment> findByBankCard_Accounter_PrivateIdAndPaymentAtBetween(String privateId, LocalDate startDate, LocalDate endDate);

}
