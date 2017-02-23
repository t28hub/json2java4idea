package io.t28.pojojson.idea.exceptions;

import javax.annotation.Nonnull;

public class InvalidDirectoryException extends ClassCreationException {
    private static final long serialVersionUID = 8602840574745874176L;

    public InvalidDirectoryException(@Nonnull String message) {
        super(message);
    }
}
