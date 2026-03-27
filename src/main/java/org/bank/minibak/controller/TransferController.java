package org.bank.minibak.controller;

import org.bank.minibak.dto.requests.TransferRequest;
import org.bank.minibak.dto.requests.TransferScheduleRequest;
import org.bank.minibak.model.Transfer;
import org.bank.minibak.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @PutMapping("/schedule")
    public ResponseEntity<Transfer> scheduleTransfer(@RequestBody TransferScheduleRequest transferScheduleRequest, Principal principal) {
        Transfer scheduledTransfer = transferService.scheduleTransfer(transferScheduleRequest, principal);

        return new ResponseEntity<>(scheduledTransfer, HttpStatus.OK);
    }

    @PostMapping("/cancel/{transferID}")
    public ResponseEntity<Transfer> cancelTransfer(@PathVariable UUID transferID, Principal principal) {
        Transfer cancelledTransfer = transferService.cancelTransfer(transferID, principal);

        return new ResponseEntity<>(cancelledTransfer, HttpStatus.OK);
    }

    @GetMapping("/{transferID}")
    public ResponseEntity<Transfer> getTransfer(@PathVariable UUID transferID, Principal principal) {
        Transfer transfer = transferService.getTransfer(transferID, principal);

        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<Transfer>> getTransferHistory(
            @RequestParam(required = false) Integer days,
            @PageableDefault(page = 0, size = 30, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable,
            Principal principal) {

        Page<Transfer> history = transferService.getTransfersHistory(days, pageable,principal);

        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
