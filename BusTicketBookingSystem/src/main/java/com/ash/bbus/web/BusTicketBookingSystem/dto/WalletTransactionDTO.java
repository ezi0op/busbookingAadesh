package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.TransactionType;

@Getter
@Setter
@NoArgsConstructor
public class WalletTransactionDTO {

    private Long id;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private TransactionType type;
    private String description;
    private Long referenceId;
    private LocalDateTime createdAt;

}
