package com.example.apirestchakray.request;

import java.util.List;

import com.example.apirestchakray.model.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
	
	private String email;
	private String name;
	private String phone;
	private String password;
	private String taxId;
	private List<Address> addresses;
}
