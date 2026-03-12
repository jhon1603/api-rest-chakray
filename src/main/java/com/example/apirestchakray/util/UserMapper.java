package com.example.apirestchakray.util;

import com.example.apirestchakray.dto.UserDTO;
import com.example.apirestchakray.request.CreateUserRequest;
import com.example.apirestchakray.response.UserResponse;

public final class UserMapper {

	private UserMapper() {
	}

	public static UserDTO toDto(CreateUserRequest request, String encryptedPassword, String createdAt) {
		return UserDTO.builder().email(request.getEmail()).name(request.getName()).phone(request.getPhone())
				.password(encryptedPassword).tax_id(request.getTaxId()).created_at(createdAt)
				.addresses(request.getAddresses()).build();
	}

	public static UserResponse toResponse(UserDTO dto) {
		return UserResponse.builder().id(dto.getId()).email(dto.getEmail()).name(dto.getName()).phone(dto.getPhone())
				.tax_id(dto.getTax_id()).created_at(dto.getCreated_at()).addresses(dto.getAddresses()).build();
	}
}