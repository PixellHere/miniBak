package org.bank.minibak.controller;

import org.bank.minibak.model.Transfer;
import org.bank.minibak.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(@RequestBody Transfer transfer) {
        Transfer createdTransfer = transferService.createTransfer(transfer);

        return new ResponseEntity<>(createdTransfer, HttpStatus.CREATED);
    }

    @PostMapping("/send/{transferID}")
    public ResponseEntity<Transfer> sendTransfer(@PathVariable UUID transferID) {
        Transfer createdTransfer = transferService.sendTransfer(transferID);

        return new ResponseEntity<>(createdTransfer, HttpStatus.OK);
    }
}
