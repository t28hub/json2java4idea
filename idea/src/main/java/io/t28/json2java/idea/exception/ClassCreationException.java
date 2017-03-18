package io.t28.json2java.idea.exception;

import javax.annotation.Nonnull;

public class ClassCreationException extends Exception {
    public ClassCreationException(@Nonnull String message) {
        super(message);
    }

    public ClassCreationException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
