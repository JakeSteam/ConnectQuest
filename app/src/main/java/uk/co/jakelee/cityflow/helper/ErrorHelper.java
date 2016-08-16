package uk.co.jakelee.cityflow.helper;

import uk.co.jakelee.cityflow.model.Text;

public class ErrorHelper {
    public enum Error {
        FailedToConnect
    }

    public static String getError(Error error) {
        return Text.get("ERROR_", error.toString());
    }
}
