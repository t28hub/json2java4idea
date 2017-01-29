package io.t28.model.json.core.io.exception;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JsonReadException extends IOException {
    private static final long serialVersionUID = 1477109671450199015L;

    public JsonReadException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
