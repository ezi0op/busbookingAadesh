package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.RegisterDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.UserResponseDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.UserUpdateDTO;

import java.util.List;

public interface UserService {

	UserResponseDTO register(RegisterDTO dto);

	// ✅ New — admin registration
	UserResponseDTO registerAdmin(RegisterDTO dto, String secretKey);

	UserResponseDTO getUserById(Long id);

	UserResponseDTO getUserByEmail(String email);

	List<UserResponseDTO> getAllUsers();

	UserResponseDTO updateUser(Long id, UserUpdateDTO dto);

	void deleteUser(Long id);
}