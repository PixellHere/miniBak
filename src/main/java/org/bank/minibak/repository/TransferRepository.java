package org.bank.minibak.repository;

import org.bank.minibak.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    boolean existsByID(UUID id);

    @Query("SELECT t FROM Transfer t WHERE (t.senderTag = :tag OR t.recipientTag = :tag)" +
    "AND t.dateTime >= :startDate AND t.dateTime < :endDate")
    Page<Transfer> findTransferHistory(
            @Param("tag") String tag,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
