package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.dto.ChatRequestDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.ChatResponseDTO;
import com.ash.bbus.web.BusTicketBookingSystem.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping
    public ResponseEntity<ChatResponseDTO> chat(@RequestBody ChatRequestDTO request) {
        return ResponseEntity.ok(chatbotService.chat(request));
    }
}
