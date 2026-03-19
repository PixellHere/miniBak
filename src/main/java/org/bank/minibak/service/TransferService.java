package org.bank.minibak.service;

import org.bank.minibak.model.Client;
import org.bank.minibak.model.Transfer;
import org.bank.minibak.model.TransferStatus;
import org.bank.minibak.repository.ClientRepository;
import org.bank.minibak.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final ClientRepository clientRepository;

    public TransferService(TransferRepository transferRepository, ClientRepository clientRepository) {
        this.transferRepository = transferRepository;
        this.clientRepository = clientRepository;
    }

    public Transfer createTransfer(Transfer transfer) {
        if(clientRepository.findByTransferTag(transfer.getRecipientTag()).isEmpty()) {
            throw new RuntimeException("Recipient tag does not exist");
        }

        if(transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        return transferRepository.save(transfer);
    }

    public Transfer sendTransfer(UUID transferID) {

        if(!transferRepository.existsByID(transferID)) {
            throw new RuntimeException("Transfer does not exist");
        }

        Transfer transfer = transferRepository.getReferenceById(transferID);

        if (transfer.getStatus() != TransferStatus.DRAFT) {
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
}
