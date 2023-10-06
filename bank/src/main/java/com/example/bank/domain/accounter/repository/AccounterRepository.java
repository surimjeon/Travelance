package com.example.bank.domain.accounter.repository;

import com.example.bank.domain.accounter.entity.Accounter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccounterRepository extends JpaRepository<Accounter, Long> {


    Accounter findByName(String name);

    Optional<Accounter> findByPrivateId(String privateId);


}
