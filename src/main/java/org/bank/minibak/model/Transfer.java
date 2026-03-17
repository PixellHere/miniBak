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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    protected Transfer() {}

    public Transfer(String senderTag, BigDecimal amount, String recipientTag, String description, String attachment) {
        this.senderTag = senderTag;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        this.recipientTag = recipientTag;
        this.description = description;
        this.attachment = attachment;
        this.status = TransferStatus.DRAFT;
    }

    public UUID getID() {
        return ID;
    }

    public String getSenderTag() {
        return senderTag;
    }

    public void setSenderTag(String senderTag) {
        this.senderTag = senderTag;
    }

    public String getRecipientTag() {
        return recipientTag;
    }

    public void setRecipientTag(String recipientTag) {
        this.recipientTag = recipientTag;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus transferStatus) {
        this.status = transferStatus;
    }
}