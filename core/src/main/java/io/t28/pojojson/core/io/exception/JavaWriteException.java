package io.t28.pojojson.core.io.exception;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JavaWriteException extends IOException {
    private static final long serialVersionUID = 5642915429649238000L;

    public JavaWriteException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
