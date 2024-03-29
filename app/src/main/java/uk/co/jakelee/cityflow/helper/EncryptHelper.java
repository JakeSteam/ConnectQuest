package uk.co.jakelee.cityflow.helper;

import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.SupportCode;

public class EncryptHelper {
    private static final String encryptionPwd = "Please don't cheat! " + "It ruins the game for others, " + "and yourself!";

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
            Log.d("DecryptionError1", e.toString());
        }
        return encrypted;
    }

    public static long decodeToLong(String encrypted, int salt) {
        String result = decode(encrypted, salt);
        return result.equals("") ? 0 : Long.parseLong(result);
    }

    public static int decodeToInt(String encrypted, int salt) {
        String result = decode(encrypted, salt);
        return result.equals("") ? 0 : Integer.parseInt(decode(encrypted, salt));
    }

    public static boolean decodeToBool(String encrypted, int salt) {
        String returned = decode(encrypted, salt);
        return returned.equals("true");
    }

    public static String decode(String encrypted, int salt) {
        String plaintext = "";
        try {
            plaintext = AESCrypt.decrypt(encryptionPwd + salt, encrypted);
        } catch (GeneralSecurityException e) {
            Log.d("DecryptionError2", e.toString());
        }
        return plaintext;
    }

    public static boolean applyCode(String code) {
        boolean successful = false;
        String decodedString = decode(code);
        String[] parts = decodedString.split("\\|");
        if (validatePartsAndCode(parts)) {
            String[] queries = parts[1].split(";");
            for (String query : queries) {
                if (query.length() > 0) {
                    query = query.trim();
                    Statistic.executeQuery(query);
                }
            }
            successful = true;
        }

        if (successful) {
            (new SupportCode(code)).save();
        }
        return successful;
    }

    private static boolean validatePartsAndCode(String[] parts) {
        if (parts.length < 2 || parts[0].equals("") || parts[1].equals("")) {
            return false;
        }

        long codedTime;
        try {
            codedTime = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        return codedTime >= System.currentTimeMillis();
    }

    public static byte[] encode(byte[] plainBytes) {
        return encode(new String(plainBytes)).getBytes();
    }

    public static String encode(String plaintext) {
        String encrypted = "";
        try {
            encrypted = AESCrypt.encrypt(encryptionPwd, plaintext);
        } catch (GeneralSecurityException e) {
            Log.d("DecryptionError3", e.toString());
        }
        return encrypted;
    }

    public static String decode(String encrypted) {
        String plaintext = "";
        try {
            plaintext = AESCrypt.decrypt(encryptionPwd, encrypted);
        } catch (GeneralSecurityException|IllegalArgumentException e) {
            Log.d("DecryptionError4", e.toString());
        }
        return plaintext;
    }
}
