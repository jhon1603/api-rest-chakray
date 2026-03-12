package com.example.apirestchakray.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.apirestchakray.dto.UserDTO;

public interface UserRepository {

	List<UserDTO> findAll();
    Optional<UserDTO> findById(UUID id);
    Optional<UserDTO> findByTaxId(String taxId);
    UserDTO save(UserDTO user);
    void deleteById(UUID id);
    boolean existsByTaxId(String taxId);
}
