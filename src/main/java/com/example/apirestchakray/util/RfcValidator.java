package com.example.apirestchakray.util;

import java.util.regex.Pattern;

public final class RfcValidator {

	private static final Pattern RFC_PATTERN = Pattern
			.compile("^([A-ZÑ&]{3,4})(\\d{2})(0[1-9]|1[0-2])([0-2][0-9]|3[0-1])([A-Z\\d]{3})$");

	private RfcValidator() {
	}

	public static boolean isValid(String taxId) {
		if (taxId == null || taxId.isBlank()) {
			return false;
		}
		return RFC_PATTERN.matcher(taxId.trim().toUpperCase()).matches();
	}
}