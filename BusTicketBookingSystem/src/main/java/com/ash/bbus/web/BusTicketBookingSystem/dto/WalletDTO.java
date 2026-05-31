package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WalletDTO {

    private Long id;
    private BigDecimal balance;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;

}
