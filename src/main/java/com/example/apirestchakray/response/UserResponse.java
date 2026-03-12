package com.example.apirestchakray.response;

import java.util.List;
import java.util.UUID;

import com.example.apirestchakray.model.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

	private UUID id;
	private String email;
	private String name;
	private String phone;
	private String tax_id;
	private String created_at;
	private List<Address> addresses;
}
