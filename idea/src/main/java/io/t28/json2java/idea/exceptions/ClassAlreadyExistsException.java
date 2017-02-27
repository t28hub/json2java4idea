package io.t28.json2java.idea.exceptions;

import javax.annotation.Nonnull;

public class ClassAlreadyExistsException extends ClassCreationException {
    public ClassAlreadyExistsException(@Nonnull String message) {
        super(message);
    }
}
