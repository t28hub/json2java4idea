package io.t28.pojojson.idea.exceptions;

import javax.annotation.Nonnull;

public class ClassCreationException extends Exception {
    public ClassCreationException(@Nonnull String message) {
        super(message);
    }

    public ClassCreationException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
