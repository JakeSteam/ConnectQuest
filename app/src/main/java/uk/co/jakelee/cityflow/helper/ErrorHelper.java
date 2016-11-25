package uk.co.jakelee.cityflow.helper;

import uk.co.jakelee.cityflow.model.Text;

public class ErrorHelper {
    public enum Error {
        NO_ERROR, ADVERT_NOT_LOADED, ADVERT_NOT_VERIFIED, FAILED_TO_CONNECT, SUPPORT_CODE_INVALID,
        NOT_ENOUGH_CURRENCY, MAX_PURCHASES, TECHNICAL, CLOUD_ERROR, PUZZLE_NOT_TESTED,
        PUZZLE_TOO_SMALL, CARD_NOT_SAVED, CAMERA_IMPORT_FAIL, FILE_IMPORT_FAIL, BACKGROUND_LOCKED,
        NO_IAB, IAB_FAILED, TEXT_IMPORT_FAIL, SUPPORT_CODE_USED
    }

    public static String get(Error error) {
        return Text.get("ERROR_", error.toString());
    }
}
