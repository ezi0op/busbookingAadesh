package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.ChatRequestDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.ChatResponseDTO;

public interface ChatbotService {
    ChatResponseDTO chat(ChatRequestDTO request);
}
