package com.aozi.util;

import java.util.regex.Pattern;

public class Util {
	public static boolean isValidEmail(String email) {
		Pattern email_pattern = Pattern.compile("[a-zA-Z0-9\\.\\+\\_\\-]{1,20}\\@[a-zA-Z0-9\\-]+\\.[a-zA-Z]+");
		return email_pattern.matcher(email).matches();
	}
}
