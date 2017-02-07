package io.t28.pojojson.core.io.exception;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JavaBuildException extends IOException {
    private static final long serialVersionUID = 5642915429649238000L;

    public JavaBuildException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
