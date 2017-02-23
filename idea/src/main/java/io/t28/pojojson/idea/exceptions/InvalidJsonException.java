package io.t28.pojojson.idea.exceptions;

import javax.annotation.Nonnull;

public class InvalidJsonException extends Exception {
    private static final long serialVersionUID = -5295819770562855261L;

    public InvalidJsonException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
