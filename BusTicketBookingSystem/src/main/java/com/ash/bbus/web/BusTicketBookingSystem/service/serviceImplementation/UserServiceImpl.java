package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.dto.RegisterDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.UserResponseDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.UserUpdateDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.User;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Wallet;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.Role;
import com.ash.bbus.web.BusTicketBookingSystem.exception.ResourceNotFoundException;
import com.ash.bbus.web.BusTicketBookingSystem.repository.UserRepository;
import com.ash.bbus.web.BusTicketBookingSystem.repository.WalletRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final WalletRepository walletRepository;
	private final PasswordEncoder passwordEncoder;
	private final String adminSecret;

	public UserServiceImpl(UserRepository userRepository, WalletRepository walletRepository,
			PasswordEncoder passwordEncoder,
			@Value("${app.admin.secret}") String adminSecret) {
		this.userRepository = userRepository;
		this.walletRepository = walletRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminSecret = adminSecret;
	}

	// ✅ Register normal user
	@Override
	@Transactional
	public UserResponseDTO register(RegisterDTO dto) {

		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new IllegalArgumentException("Email already registered: " + dto.getEmail());
		}

		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setPhone(dto.getPhone());
		user.setRole(Role.USER);

		User saved = userRepository.save(user);

		// ✅ Auto-create wallet
		Wallet wallet = Wallet.builder().user(saved).balance(java.math.BigDecimal.ZERO).build();
		walletRepository.save(wallet);

		return mapToDTO(saved);
	}

	// ✅ Register admin — secret key required
	@Override
	@Transactional
	public UserResponseDTO registerAdmin(RegisterDTO dto, String secretKey) {

		// ✅ Verify secret key
		if (!adminSecret.equals(secretKey)) {
			throw new IllegalArgumentException("Invalid secret key");
		}

		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new IllegalArgumentException("Email already registered: " + dto.getEmail());
		}

		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setPhone(dto.getPhone());
		user.setRole(Role.ADMIN); // ✅ ADMIN role

		User saved = userRepository.save(user);

		// ✅ Auto-create wallet for admin
		Wallet wallet = Wallet.builder().user(saved).balance(java.math.BigDecimal.ZERO).build();
		walletRepository.save(wallet);

		return mapToDTO(saved);
	}

	@Override
	public UserResponseDTO getUserById(Long id) {
		return mapToDTO(findUser(id));
	}

	@Override
	public UserResponseDTO getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
		return mapToDTO(user);
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {

		User user = findUser(id);

		if (dto.getName() != null && !dto.getName().isBlank()) {
			user.setName(dto.getName());
		}
		if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
			user.setPhone(dto.getPhone());
		}
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		return mapToDTO(userRepository.save(user));
	}

	@Override
	@Transactional
	public void deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User not found: " + id);
		}
		userRepository.deleteById(id);
	}

	private User findUser(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
	}

	private UserResponseDTO mapToDTO(User user) {
		return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(),
				user.getRole().name(), user.getCreatedAt());
	}
}
