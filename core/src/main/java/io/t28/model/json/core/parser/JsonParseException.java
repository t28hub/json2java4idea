package io.t28.model.json.core.parser;

import javax.annotation.Nonnull;

public class JsonParseException extends RuntimeException {
    private static final long serialVersionUID = 1477109671450199015L;

    public JsonParseException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
