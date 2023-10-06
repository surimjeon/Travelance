package com.example.bank.domain.bankcard.repository;

import com.example.bank.domain.accounter.entity.Accounter;
import com.example.bank.domain.bankcard.entity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankCardRepository extends JpaRepository<BankCard,Long> {
    List<BankCard> findByAccounterPrivateId(String privateId);

    List<BankCard> findByAccounterId(Optional<Accounter> accounterId);

    Optional<BankCard> findByCardNumberAndCvc(String cardNumber, String cvc);
}
