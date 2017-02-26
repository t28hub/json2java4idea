package io.t28.pojojson.idea.exceptions;

import javax.annotation.Nonnull;

public class JsonFormatException extends Exception {
    private static final long serialVersionUID = -3657295542306947657L;

    public JsonFormatException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
