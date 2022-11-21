package com.example.demodiaop.adapter.dao.repo;

import com.example.demodiaop.adapter.dao.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    Optional<AccountEntity> findByUsername(String username);
}
