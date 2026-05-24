package com.ash.bbus.web.BusTicketBookingSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ash.bbus.web.BusTicketBookingSystem.dto.ApiResponse;
import com.ash.bbus.web.BusTicketBookingSystem.dto.AuthResponseDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.LoginDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.RegisterDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.UserResponseDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.UserUpdateDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.User;
import com.ash.bbus.web.BusTicketBookingSystem.service.UserService;
import com.ash.bbus.web.BusTicketBookingSystem.util.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class UserController {

	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	// ✅ POST /api/users/register
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody RegisterDTO dto) {

		UserResponseDTO user = userService.register(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Registration successful", user));
	}

	// ✅ POST /api/users/register-admin
	@PostMapping("/register-admin")
	public ResponseEntity<ApiResponse<UserResponseDTO>> registerAdmin(@Valid @RequestBody RegisterDTO dto,
			@RequestParam String secretKey) {

		UserResponseDTO user = userService.registerAdmin(dto, secretKey);

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Admin registered", user));
	}

	// ✅ POST /api/users/login
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginDTO dto) {

		try {
			Authentication auth = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

			User user = (User) auth.getPrincipal();
			String token = jwtUtil.generateToken(user);

			AuthResponseDTO response = new AuthResponseDTO(token, user.getId(), user.getName(), user.getEmail(),
					user.getRole().name());

			return ResponseEntity.ok(ApiResponse.success("Login successful", response));

		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.failure("Invalid email or password"));
		}
	}

	// ✅ GET /api/users — ADMIN only
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {

		return ResponseEntity.ok(ApiResponse.success("Users fetched", userService.getAllUsers()));
	}

	// ✅ GET /api/users/{id}
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or " + "#id == authentication.principal.id")
	public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {

		return ResponseEntity.ok(ApiResponse.success("User fetched", userService.getUserById(id)));
	}

	// ✅ PUT /api/users/{id}
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or " + "#id == authentication.principal.id")
	public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@PathVariable Long id,
			@Valid @RequestBody UserUpdateDTO dto) {

		return ResponseEntity.ok(ApiResponse.success("User updated", userService.updateUser(id, dto)));
	}

	// ✅ DELETE /api/users/{id} — ADMIN only
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {

		userService.deleteUser(id);
		return ResponseEntity.ok(ApiResponse.success("User deleted"));
	}
}