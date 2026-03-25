package com.bank.api.infrastructure.repository;

import com.bank.api.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select a from Account a where (:accountId is null or a.id=:accountId)")
    Page<Account> findAllByIdOptional(@Param("accountId") Long id, Pageable pageable);
}
