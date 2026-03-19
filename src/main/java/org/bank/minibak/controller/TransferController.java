package org.bank.minibak.controller;

import org.bank.minibak.dto.requests.TransferRequest;
import org.bank.minibak.model.Transfer;
import org.bank.minibak.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(@RequestBody TransferRequest transferRequest, Principal principal) {
        Transfer createdTransfer = transferService.createTransfer(transferRequest, principal);

        return new ResponseEntity<>(createdTransfer, HttpStatus.CREATED);
    }

    @PostMapping("/send/{transferID}")
    public ResponseEntity<Transfer> sendTransfer(@PathVariable UUID transferID, Principal principal) {
        Transfer createdTransfer = transferService.sendTransfer(transferID, principal);

        return new ResponseEntity<>(createdTransfer, HttpStatus.OK);
    }
}
