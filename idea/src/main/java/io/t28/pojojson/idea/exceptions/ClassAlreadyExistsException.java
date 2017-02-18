package io.t28.pojojson.idea.exceptions;

import javax.annotation.Nonnull;

public class ClassAlreadyExistsException extends ClassCreationException {
    public ClassAlreadyExistsException(@Nonnull String message) {
        super(message);
    }
}
