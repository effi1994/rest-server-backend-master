package com.dev.utils;

import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Utils {

    public boolean validateUsername (String username) {
        boolean valid = false;
        if (username != null) {
            if (username.contains("@")) {
                valid = true;
            }
        }
        return valid;
    }

    public boolean validatePassword (String password) {
        boolean valid = false;
        if (password != null) {
            if (password.length() >= 8) {
                valid = true;
            }
        }
        return valid;
    }

    public boolean validateNote (String note) {
        boolean valid = false;
        if (note != null && note.length() > 0) {
            valid = true;
        }
        return valid;
    }

    public String createHash (String username, String password) {
        String myHash;
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String raw = String.format("%s_%s", username, password);
            sha256.update(raw.getBytes());
            byte[] digest = sha256.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return myHash;
    }
}
