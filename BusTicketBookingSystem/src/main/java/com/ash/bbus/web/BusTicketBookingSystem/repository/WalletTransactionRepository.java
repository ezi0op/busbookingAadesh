package com.ash.bbus.web.BusTicketBookingSystem.repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.WalletTransaction;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WalletTransactionRepository
        extends JpaRepository<WalletTransaction, Long> {

    // ✅ Latest transactions first
    List<WalletTransaction> findByWalletIdOrderByCreatedAtDesc(
        Long walletId
    );

    List<WalletTransaction> findByWalletIdAndType(
        Long walletId, TransactionType type
    );
}