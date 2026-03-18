package org.bank.minibak.repository;

import org.bank.minibak.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    boolean existsByID(UUID id);
}
