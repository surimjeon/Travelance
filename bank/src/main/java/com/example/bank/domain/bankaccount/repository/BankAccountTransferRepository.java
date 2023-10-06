package com.example.bank.domain.bankaccount.repository;

import com.example.bank.domain.bankaccount.entity.BankAccount;
import com.example.bank.domain.bankaccount.entity.BankAccountTransfer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BankAccountTransferRepository extends JpaRepository<BankAccountTransfer, Long> {
    @Query("SELECT t FROM BankAccountTransfer t WHERE t.depositNumber = :account OR t.withdrawalNumber = :account")
    List<BankAccountTransfer> findByAccountNumber(@Param("account") String account);

    @Query("SELECT t.memo FROM BankAccountTransfer t WHERE t.depositNumber = :depositNumber AND t.withdrawalNumber = :withdrawalNumber ORDER BY t.transferAt DESC")
    List<String> findLatestMemoByDepositNumberAndWithdrawalNumber(@Param("depositNumber") String depositNumber, @Param("withdrawalNumber") String withdrawalNumber);


    @Query("SELECT bat FROM BankAccountTransfer bat " +
            "WHERE bat.depositNumber = :depositNumber " +
            "AND bat.withdrawalNumber = :withdrawalNumber " +
            "AND bat.transferAt = (SELECT MAX(bat2.transferAt) FROM BankAccountTransfer bat2 " +
            "WHERE bat2.depositNumber = :depositNumber AND bat2.withdrawalNumber = :withdrawalNumber)")
    BankAccountTransfer findLatestMatchingTransfer(String depositNumber, String withdrawalNumber);

}