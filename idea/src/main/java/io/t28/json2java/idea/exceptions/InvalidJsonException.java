package io.t28.json2java.idea.exceptions;

import javax.annotation.Nonnull;

public class InvalidJsonException extends JsonFormatException {
    private static final long serialVersionUID = -5295819770562855261L;

    public InvalidJsonException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
