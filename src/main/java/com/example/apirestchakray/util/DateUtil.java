package com.example.apirestchakray.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

	private static final ZoneId MADAGASCAR_ZONE = ZoneId.of("Indian/Antananarivo");
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

	private DateUtil() {
	}

	public static String getCurrentMadagascarTimestamp() {
		return LocalDateTime.now(MADAGASCAR_ZONE).format(FORMATTER);
	}
}