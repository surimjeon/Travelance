package com.example.bank.domain.bankaccount.repository;

import com.example.bank.domain.accounter.entity.Accounter;
import com.example.bank.domain.bankaccount.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    BankAccount findByAccounter(Accounter accounter);
    Optional<BankAccount> findByAccount(String account);

    List<BankAccount> findByAccounterId(Long accounterId);

    BankAccount findBankAccountByAccount(String account);
    @Query("SELECT ba.accounter.privateId FROM BankAccount ba WHERE ba.account = :accountNumber")
    String findPrivateIdByAccountNumber(@Param("accountNumber") String accountNumber);

}
