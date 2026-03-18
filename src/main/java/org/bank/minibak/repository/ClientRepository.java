package org.bank.minibak.repository;

import org.bank.minibak.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    boolean existsByEmail(String email);
    Optional<Client> findByTransferTag(String transferTag);
}
