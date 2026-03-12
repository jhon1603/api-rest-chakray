package com.example.apirestchakray.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.apirestchakray.dto.UserDTO;
import com.example.apirestchakray.exception.BadRequestException;
import com.example.apirestchakray.exception.ResourceNotFoundException;
import com.example.apirestchakray.exception.UnauthorizedException;
import com.example.apirestchakray.repository.UserRepository;
import com.example.apirestchakray.request.CreateUserRequest;
import com.example.apirestchakray.request.LoginRequest;
import com.example.apirestchakray.request.UpdateUserRequest;
import com.example.apirestchakray.response.LoginResponse;
import com.example.apirestchakray.response.UserResponse;
import com.example.apirestchakray.util.AesUtil;
import com.example.apirestchakray.util.DateUtil;
import com.example.apirestchakray.util.PhoneValidator;
import com.example.apirestchakray.util.RfcValidator;
import com.example.apirestchakray.util.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public List<UserResponse> getUsers(String sortedBy, String filter) {
		List<UserDTO> users = userRepository.findAll();

		if (filter != null) {
			if (filter.isBlank()) {
				throw new BadRequestException("El parámetro 'filter' no debe ser vacío o nulo");
			}

			users = applyFilter(users, filter);
		}

		if (sortedBy != null && !sortedBy.isBlank()) {
			users = sortUsers(users, sortedBy);
		}

		return users.stream().map(UserMapper::toResponse).toList();
	}

	public UserResponse createUser(CreateUserRequest request) {
		validateCreateRequest(request);

		if (userRepository.existsByTaxId(request.getTaxId())) {
			throw new BadRequestException("El taxId ya existe");
		}

		UserDTO UserDTO = UserMapper.toDto(request, AesUtil.encrypt(request.getPassword()),
				DateUtil.getCurrentMadagascarTimestamp());

		UserDTO saved = userRepository.save(UserDTO);
		return UserMapper.toResponse(saved);
	}

	public UserResponse updateUser(UUID id, UpdateUserRequest request) {
		UserDTO existing = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

		if (request.getEmail() != null && !request.getEmail().isBlank()) {
			existing.setEmail(request.getEmail());
		}

		if (request.getName() != null && !request.getName().isBlank()) {
			existing.setName(request.getName());
		}

		if (request.getPhone() != null && !request.getPhone().isBlank()) {
			if (!PhoneValidator.isValid(request.getPhone())) {
				throw new BadRequestException("El phone no tiene un formato válido");
			}
			existing.setPhone(request.getPhone());
		}

		if (request.getPassword() != null && !request.getPassword().isBlank()) {
			existing.setPassword(AesUtil.encrypt(request.getPassword()));
		}

		if (request.getTaxId() != null && !request.getTaxId().isBlank()) {
			if (!RfcValidator.isValid(request.getTaxId())) {
				throw new BadRequestException("El taxId no tiene un formato RFC válido");
			}

			userRepository.findByTaxId(request.getTaxId()).ifPresent(user -> {
				if (!user.getId().equals(id)) {
					throw new BadRequestException("El taxId ya existe");
				}
			});

			existing.setTax_id(request.getTaxId().toUpperCase(Locale.ROOT));
		}

		if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
			existing.setAddresses(request.getAddresses());
		}

		UserDTO updated = userRepository.save(existing);
		return UserMapper.toResponse(updated);
	}

	public void deleteUser(UUID id) {
		userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

		userRepository.deleteById(id);
	}

	public LoginResponse login(LoginRequest request) {
		if (request == null || isBlank(request.getTax_id()) || isBlank(request.getPassword())) {
			throw new BadRequestException("taxId y password son obligatorios");
		}

		UserDTO user = userRepository.findByTaxId(request.getTax_id())
				.orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

		if (!AesUtil.matches(request.getPassword(), user.getPassword())) {
			throw new UnauthorizedException("Credenciales inválidas");
		}

		return LoginResponse.builder().success(true).message("Login exitoso").build();
	}

	private void validateCreateRequest(CreateUserRequest request) {
		if (request == null) {
			throw new BadRequestException("El body de la petición es obligatorio");
		}

		if (isBlank(request.getEmail())) {
			throw new BadRequestException("El email es obligatorio");
		}

		if (isBlank(request.getName())) {
			throw new BadRequestException("El name es obligatorio");
		}

		if (isBlank(request.getPhone())) {
			throw new BadRequestException("El phone es obligatorio");
		}

		if (isBlank(request.getPassword())) {
			throw new BadRequestException("El password es obligatorio");
		}

		if (isBlank(request.getTaxId())) {
			throw new BadRequestException("El taxId es obligatorio");
		}

		if (!PhoneValidator.isValid(request.getPhone())) {
			throw new BadRequestException("El phone no tiene un formato válido");
		}

		if (!RfcValidator.isValid(request.getTaxId())) {
			throw new BadRequestException("El taxId no tiene un formato RFC válido");
		}

		request.setTaxId(request.getTaxId().toUpperCase(Locale.ROOT));
	}

//	private boolean containsValue(UserDTO user, String criteria) {
//		return safe(user.getEmail()).contains(criteria) || safe(user.getName()).contains(criteria)
//				|| safe(user.getPhone()).contains(criteria) || safe(user.getTax_id()).contains(criteria)
//				|| safe(user.getCreated_at()).contains(criteria);
//	}

	private List<UserDTO> sortUsers(List<UserDTO> users, String sortedBy) {
		String sortField = sortedBy.trim().toLowerCase(Locale.ROOT);

		Comparator<UserDTO> comparator = switch (sortField) {
		case "email" -> Comparator.comparing(user -> safe(user.getEmail()));
		case "name" -> Comparator.comparing(user -> safe(user.getName()));
		case "phone" -> Comparator.comparing(user -> safe(user.getPhone()));
		case "taxid", "tax_id" -> Comparator.comparing(user -> safe(user.getTax_id()));
		case "createdat", "created_at" -> Comparator.comparing(user -> safe(user.getCreated_at()));
		default -> throw new BadRequestException("Campo de ordenamiento no soportado: " + sortedBy);
		};

		return users.stream().sorted(comparator).collect(Collectors.toList());
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	private String safe(String value) {
		return value == null ? "" : value.toLowerCase(Locale.ROOT);
	}

	private List<UserDTO> applyFilter(List<UserDTO> users, String filter) {
		String[] parts = filter.split("\\+", 3);

		if (parts.length != 3) {
			throw new BadRequestException("El parámetro 'filter' debe tener el formato atributo+operador+valor");
		}

		String attribute = parts[0].trim().toLowerCase(Locale.ROOT);
		String operator = parts[1].trim().toLowerCase(Locale.ROOT);
		String value = parts[2].trim();

		if (attribute.isBlank() || operator.isBlank() || value.isBlank()) {
			throw new BadRequestException("El parámetro 'filter' debe contener atributo, operador y valor");
		}

		return users.stream().filter(user -> matchesFilter(user, attribute, operator, value)).toList();
	}

	private boolean matchesFilter(UserDTO user, String attribute, String operator, String value) {
		String userValue = getAttributeValue(user, attribute);
		String normalizedUserValue = userValue.toLowerCase(Locale.ROOT);
		String normalizedFilterValue = value.toLowerCase(Locale.ROOT);

		return switch (operator) {
		case "co" -> normalizedUserValue.contains(normalizedFilterValue);
		case "eq" -> normalizedUserValue.equals(normalizedFilterValue);
		case "sw" -> normalizedUserValue.startsWith(normalizedFilterValue);
		case "ew" -> normalizedUserValue.endsWith(normalizedFilterValue);
		default -> throw new BadRequestException("Operador de filtro no soportado. Usa: co, eq, sw o ew");
		};
	}

	private String getAttributeValue(UserDTO user, String attribute) {
		return switch (attribute) {
		case "id" -> user.getId() != null ? user.getId().toString() : "";
		case "email" -> safe(user.getEmail());
		case "name" -> safe(user.getName());
		case "phone" -> safe(user.getPhone());
		case "tax_id", "taxid" -> safe(user.getTax_id());
		case "created_at", "createdat" -> safe(user.getCreated_at());
		default -> throw new BadRequestException(
				"Atributo de filtro no soportado. Usa: email, id, name, phone, tax_id o created_at");
		};
	}
}