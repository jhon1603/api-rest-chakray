package com.example.apirestchakray.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.apirestchakray.dto.UserDTO;

@Repository
public class InMemoryUserRepository implements UserRepository {

	private final List<UserDTO> users = new ArrayList<>();

	@Override
	public List<UserDTO> findAll() {
		return new ArrayList<>(users);
	}

	@Override
	public Optional<UserDTO> findById(UUID id) {
		return users.stream().filter(user -> user.getId().equals(id)).findFirst();
	}

	@Override
	public Optional<UserDTO> findByTaxId(String taxId) {
		return users.stream().filter(user -> user.getTax_id().equalsIgnoreCase(taxId)).findFirst();
	}

	@Override
	public UserDTO save(UserDTO user) {
		if (user.getId() == null) {
			user.setId(UUID.randomUUID());
		}

		findById(user.getId()).ifPresent(existing -> users.remove(existing));

		users.add(user);
		return user;
	}

	@Override
	public void deleteById(UUID id) {
		users.removeIf(user -> user.getId().equals(id));
	}

	@Override
	public boolean existsByTaxId(String taxId) {
		return users.stream().anyMatch(user -> user.getTax_id().equalsIgnoreCase(taxId));
	}
}
