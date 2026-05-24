package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.dto.WalletDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.WalletTransactionDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Wallet;
import com.ash.bbus.web.BusTicketBookingSystem.entity.WalletTransaction;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.TransactionType;
import com.ash.bbus.web.BusTicketBookingSystem.exception.ResourceNotFoundException;
import com.ash.bbus.web.BusTicketBookingSystem.repository.WalletRepository;
import com.ash.bbus.web.BusTicketBookingSystem.repository.WalletTransactionRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.WalletService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;

    public WalletServiceImpl(WalletRepository walletRepository,
                              WalletTransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public WalletDTO getWalletByUser(Long userId) {
        return mapToDTO(findWallet(userId));
    }

    @Override
    @Transactional
    public WalletDTO creditWallet(Long userId, BigDecimal amount,
                                   String description, Long referenceId) {
        Wallet wallet = findWallet(userId);
        wallet.credit(amount);  // ✅ uses entity helper

        WalletTransaction txn = WalletTransaction.builder()
            .amount(amount)
            .balanceAfter(wallet.getBalance())
            .type(TransactionType.CREDIT)
            .description(description)
            .referenceId(referenceId)
            .wallet(wallet)
            .build();
        transactionRepository.save(txn);

        return mapToDTO(walletRepository.save(wallet));
    }

    @Override
    @Transactional
    public WalletDTO debitWallet(Long userId, BigDecimal amount,
                                  String description, Long referenceId) {
        Wallet wallet = findWallet(userId);
        wallet.debit(amount);   // ✅ uses entity helper — checks balance

        WalletTransaction txn = WalletTransaction.builder()
            .amount(amount)
            .balanceAfter(wallet.getBalance())
            .type(TransactionType.DEBIT)
            .description(description)
            .referenceId(referenceId)
            .wallet(wallet)
            .build();
        transactionRepository.save(txn);

        return mapToDTO(walletRepository.save(wallet));
    }

    @Override
    public List<WalletTransactionDTO> getTransactionHistory(Long userId) {
        Wallet wallet = findWallet(userId);
        return transactionRepository
            .findByWalletIdOrderByCreatedAtDesc(wallet.getId())
            .stream()
            .map(this::mapTxnToDTO)
            .collect(Collectors.toList());
    }

    private Wallet findWallet(Long userId) {
        return walletRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Wallet not found for user: " + userId
            ));
    }

    private WalletDTO mapToDTO(Wallet wallet) {
        WalletDTO dto = new WalletDTO();
        dto.setId(wallet.getId());
        dto.setBalance(wallet.getBalance());
        dto.setUserId(wallet.getUser().getId());
        dto.setUserName(wallet.getUser().getName());
        dto.setCreatedAt(wallet.getCreatedAt());
        return dto;
    }

    private WalletTransactionDTO mapTxnToDTO(WalletTransaction txn) {
        WalletTransactionDTO dto = new WalletTransactionDTO();
        dto.setId(txn.getId());
        dto.setAmount(txn.getAmount());
        dto.setBalanceAfter(txn.getBalanceAfter());
        dto.setType(txn.getType());
        dto.setDescription(txn.getDescription());
        dto.setReferenceId(txn.getReferenceId());
        dto.setCreatedAt(txn.getCreatedAt());
        return dto;
    }
}