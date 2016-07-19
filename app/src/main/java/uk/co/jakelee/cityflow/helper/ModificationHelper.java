package uk.co.jakelee.cityflow.helper;

import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class ModificationHelper {
    private static final String encryptionPwd = "This " + "should probably be " + "stored better!";

    public static String encode(Long unencrypted, int salt) {
        return encode(Long.toString(unencrypted), salt);
    }

    public static String encode(Integer unencrypted, int salt) {
        return encode(Integer.toString(unencrypted), salt);
    }

    public static String encode(Boolean unencrypted, int salt) {
        return encode(unencrypted ? "true" : "false", salt);
    }

    public static String encode(String plaintext, int salt) {
        String encrypted = "";
        try {
            encrypted = AESCrypt.encrypt(encryptionPwd + salt, plaintext);
        } catch (GeneralSecurityException e) {
            Log.d("Blacksmith", e.toString());
        }
        return encrypted;
    }

    public static long decodeToLong(String encrypted, int salt) {
        return Long.parseLong(decode(encrypted, salt));
    }

    public static int decodeToInt(String encrypted, int salt) {
        return Integer.parseInt(decode(encrypted, salt));
    }

    public static boolean decodeToBool(String encrypted, int salt) {
        return decode(encrypted, salt).equals("true");
    }

    public static String decode(String encrypted, int salt) {
        String plaintext = "";
        try {
            plaintext = AESCrypt.decrypt(encryptionPwd + salt, encrypted);
        } catch (GeneralSecurityException e) {
            Log.d("Blacksmith", e.toString());
        }
        return plaintext;
    }
}
