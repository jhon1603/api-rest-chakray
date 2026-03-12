package com.example.apirestchakray.util;

public final class PhoneValidator {

	private PhoneValidator() {
	}

	public static boolean isValid(String phone) {
		if (phone == null || phone.isBlank()) {
			return false;
		}

		String normalized = phone.replaceAll("[^\\d]", "");

		return normalized.length() == 10 || normalized.length() > 10;
	}
}