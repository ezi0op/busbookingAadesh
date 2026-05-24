package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.WalletDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.WalletTransactionDTO;
import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    WalletDTO getWalletByUser(Long userId);
    WalletDTO creditWallet(Long userId, BigDecimal amount, 
                           String description, Long referenceId);
    WalletDTO debitWallet(Long userId, BigDecimal amount,
                          String description, Long referenceId);
    List<WalletTransactionDTO> getTransactionHistory(Long userId);
}