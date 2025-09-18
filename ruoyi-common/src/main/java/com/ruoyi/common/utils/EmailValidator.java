package com.ruoyi.common.utils;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_REGEX.matcher(email).matches();
    }
}
