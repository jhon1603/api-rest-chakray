package com.example.apirestchakray.config;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.apirestchakray.dto.UserDTO;
import com.example.apirestchakray.model.Address;
import com.example.apirestchakray.repository.UserRepository;
import com.example.apirestchakray.util.AesUtil;
import com.example.apirestchakray.util.DateUtil;

@Configuration
public class DataLoaderConfig {

	@Bean
	CommandLineRunner loadInitialData(UserRepository userRepository) {
		return args -> {
			if (userRepository.findAll().isEmpty()) {
				userRepository.save(UserDTO.builder().id(UUID.randomUUID()).email("user1@mail.com").name("user1")
						.phone("+1 55 555 555 55").password(AesUtil.encrypt("password1")).tax_id("AARR990101XXX")
						.created_at(DateUtil.getCurrentMadagascarTimestamp())
						.addresses(List.of(
								Address.builder().id(1).name("workaddress").street("street No. 1").country_code("UK")
										.build(),
								Address.builder().id(2).name("homeaddress").street("street No. 2").country_code("AU")
										.build()))
						.build());

				userRepository.save(UserDTO.builder().id(UUID.randomUUID()).email("user2@mail.com").name("user2")
						.phone("+52 55 444 333 22").password(AesUtil.encrypt("password2")).tax_id("BADD800101ABC")
						.created_at(DateUtil.getCurrentMadagascarTimestamp()).addresses(List.of(Address.builder().id(3)
								.name("officeaddress").street("avenue No. 10").country_code("MX").build()))
						.build());

				userRepository.save(UserDTO.builder().id(UUID.randomUUID()).email("user3@mail.com").name("user3")
						.phone("+34 91 123 45 67").password(AesUtil.encrypt("password3")).tax_id("COSC750113XYZ")
						.created_at(DateUtil.getCurrentMadagascarTimestamp()).addresses(List.of(Address.builder().id(4)
								.name("mainaddress").street("street No. 99").country_code("ES").build()))
						.build());
			}
		};
	}
}