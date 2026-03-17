package org.bank.minibak.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;

    @Column(nullable = false)
    private String senderTag;

    @Column(nullable = false)
    private String recipientTag;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String description;

    private String attachment;

    @Column(nullable = false)
    private String status;

    protected Transfer() {}

    public Transfer(String senderTag, BigDecimal amount, String recipientTag, String description, String attachment) {
        this.ID = UUID.randomUUID();
        this.senderTag = senderTag;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        this.recipientTag = recipientTag;
        this.description = description;
        this.attachment = attachment;
        this.status = "DRAFT";
    }

    public void transferMoney() {
        this.dateTime = LocalDateTime.now();
        this.status = "SENT";

        // Transfer in DB
    }
}
