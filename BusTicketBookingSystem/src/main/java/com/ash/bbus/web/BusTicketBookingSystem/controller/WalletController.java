package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.dto.ApiResponse;
import com.ash.bbus.web.BusTicketBookingSystem.dto.WalletDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.WalletTransactionDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.User;
import com.ash.bbus.web.BusTicketBookingSystem.service.WalletService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // ✅ GET /api/wallet — logged-in user's wallet balance
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<WalletDTO>> getMyWallet(
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
            ApiResponse.success("Wallet details",
                walletService.getWalletByUser(currentUser.getId()))
        );
    }

    // ✅ GET /api/wallet/transactions — transaction history
    @GetMapping("/transactions")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<WalletTransactionDTO>>>
            getTransactions(
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
            ApiResponse.success("Transaction history",
                walletService.getTransactionHistory(currentUser.getId()))
        );
    }
}