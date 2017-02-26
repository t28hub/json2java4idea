package io.t28.json2java.core.io.exception;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JsonParseException extends IOException {
    private static final long serialVersionUID = 1477109671450199015L;

    public JsonParseException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
