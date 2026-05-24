package com.ash.bbus.web.BusTicketBookingSystem.repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}