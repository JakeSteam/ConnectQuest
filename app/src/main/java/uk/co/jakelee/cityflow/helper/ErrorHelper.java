package uk.co.jakelee.cityflow.helper;

import uk.co.jakelee.cityflow.model.Text;

public class ErrorHelper {
    public enum Error {
        FAILED_TO_CONNECT, SUPPORT_CODE_INVALID
    }

    public static String get(Error error) {
        return Text.get("ERROR_", error.toString());
    }
}
