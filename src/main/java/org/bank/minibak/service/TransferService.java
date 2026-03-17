package org.bank.minibak.service;

import org.bank.minibak.model.Transfer;
import org.bank.minibak.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public Transfer createTransfer(Transfer transfer) {
        if(!transferRepository.existsByRecipientTag(transfer.getRecipientTag())) {
            throw new RuntimeException("Recipient tag does not exist");
        }

        return transferRepository.save(transfer);
    }
}
