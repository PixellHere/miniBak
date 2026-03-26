package org.bank.minibak.service;

import org.bank.minibak.dto.requests.TransferRequest;
import org.bank.minibak.dto.requests.TransferScheduleRequest;
import org.bank.minibak.model.Client;
import org.bank.minibak.model.Transfer;
import org.bank.minibak.model.TransferStatus;
import org.bank.minibak.repository.ClientRepository;
import org.bank.minibak.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final ClientRepository clientRepository;

    public TransferService(TransferRepository transferRepository, ClientRepository clientRepository) {
        this.transferRepository = transferRepository;
        this.clientRepository = clientRepository;
    }

    public Transfer createTransfer(TransferRequest transferRequest, Principal principal) {
        String senderEmail = principal.getName();
        Transfer transfer;
        Client client = clientRepository.findByEmail(senderEmail).orElseThrow(() -> new RuntimeException("Client not found"));

        if(clientRepository.findByTransferTag(transferRequest.recipientTag()).isEmpty()) {
            throw new RuntimeException("Recipient tag does not exist");
        }

        if(transferRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        transfer = new Transfer(client.getTransferTag(), transferRequest.amount(),
                transferRequest.recipientTag(), transferRequest.description(), transferRequest.attachment());

        return transferRepository.save(transfer);
    }

    public Transfer sendTransfer(UUID transferID, Principal principal) {

        if(!transferRepository.existsByID(transferID)) {
            throw new RuntimeException("Transfer does not exist");
        }

        String senderEmail = principal.getName();
        Client client = clientRepository.findByEmail(senderEmail).orElseThrow(() -> new RuntimeException("Client not found"));
        String senderTagFromToken = client.getTransferTag();
        Transfer transfer = transferRepository.getReferenceById(transferID);

        if(!Objects.equals(senderTagFromToken, transfer.getSenderTag())) {
            System.err.println("Sender tag from token does not match sender tag in transfer");
            throw new RuntimeException("Sender tag from token does not match sender tag in transfer");
        }

        if (transfer.getStatus() == TransferStatus.APPROVED) {
            throw new RuntimeException("Transfer already sent");
        }

        Client sender = clientRepository.findByTransferTag(transfer.getSenderTag()).orElse(null);
        Client recipient = clientRepository.findByTransferTag(transfer.getRecipientTag()).orElse(null);

        if(sender != null && recipient != null) {
            if(sender.getBalance().compareTo(transfer.getAmount()) < 0 ) {
                throw new RuntimeException("Sender balance must be greater than or even transfer amount");
            } else {
                sender.setBalance(sender.getBalance().subtract(transfer.getAmount()));
                recipient.setBalance(recipient.getBalance().add(transfer.getAmount()));

                clientRepository.save(sender);
                clientRepository.save(recipient);

                transfer.setDateTime(LocalDateTime.now());
                transfer.setStatus(TransferStatus.APPROVED);

                transferRepository.save(transfer);

                return transfer;
            }
        } else {
            throw new RuntimeException("Sender tag or recipient tag does not exist");
        }
    }

    public Transfer scheduleTransfer(TransferScheduleRequest transferSchedule, Principal principal) {
        if(!transferRepository.existsByID(transferSchedule.transferUuid())) {
            throw new RuntimeException("Transfer does not exist");
        }

        String senderEmail = principal.getName();
        Client client = clientRepository.findByEmail(senderEmail).orElseThrow(() -> new RuntimeException("Client not found"));
        String senderTagFromToken = client.getTransferTag();
        Transfer transfer = transferRepository.getReferenceById(transferSchedule.transferUuid());

        if(!Objects.equals(senderTagFromToken, transfer.getSenderTag())) {
            System.err.println("Sender tag from token does not match sender tag in transfer");
            throw new RuntimeException("Sender tag from token does not match sender tag in transfer");
        }

        if (transfer.getStatus() == TransferStatus.APPROVED) {
            throw new RuntimeException("Transfer already sent");
        }

        Client sender = clientRepository.findByTransferTag(transfer.getSenderTag()).orElse(null);
        Client recipient = clientRepository.findByTransferTag(transfer.getRecipientTag()).orElse(null);

        if(sender != null && recipient != null) {
            if(sender.getBalance().compareTo(transfer.getAmount()) < 0 ) {
                throw new RuntimeException("Sender balance must be greater than or even transfer amount");
            } else {
                if(transferSchedule.blockFunds())
                {
                    sender.setBalance(sender.getBalance().subtract(transfer.getAmount()));
                    clientRepository.save(sender);
                    transfer.setStatus(TransferStatus.SCHEDULED_BLOCKED);
                } else {
                    transfer.setStatus(TransferStatus.SCHEDULED);
                }

                transfer.setDateTime(LocalDateTime.of(transferSchedule.scheduleTime(), LocalTime.MIDNIGHT));
                transferRepository.save(transfer);

                return transfer;
            }
        } else {
            throw new RuntimeException("Sender tag or recipient tag does not exist");
        }
    }

    public Transfer cancelTransfer(UUID transferID, Principal principal) {
        if(!transferRepository.existsByID(transferID)) {
            throw new RuntimeException("Transfer does not exist");
        }

        String senderEmail = principal.getName();
        Client client = clientRepository.findByEmail(senderEmail).orElseThrow(() -> new RuntimeException("Client not found"));
        String senderTagFromToken = client.getTransferTag();
        Transfer transfer = transferRepository.getReferenceById(transferID);

        if(!Objects.equals(senderTagFromToken, transfer.getSenderTag())) {
            System.err.println("Sender tag from token does not match sender tag in transfer");
            throw new RuntimeException("Sender tag from token does not match sender tag in transfer");
        }

        if (transfer.getStatus() == TransferStatus.CANCELLED) {
            throw new RuntimeException("Transfer already cancelled");
        }

        if (transfer.getStatus() == TransferStatus.APPROVED) {
            throw new RuntimeException("Transfer already sent");
        }

        transfer.setStatus(TransferStatus.CANCELLED);
        transferRepository.save(transfer);

        return transfer;
    }
}
