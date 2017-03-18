package io.t28.json2java.idea.util;

import com.intellij.openapi.fileTypes.FileType;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public final class Extensions {
    private static final String DELIMITER = ".";

    private Extensions() {
    }

    @Nonnull
    @CheckReturnValue
    public static String remove(@Nonnull String fileName, @Nonnull FileType fileType) {
        final String extension = fileType.getDefaultExtension();
        if (fileName.endsWith(DELIMITER + extension)) {
            return fileName.substring(0, fileName.length() - (extension.length() + 1));
        }
        return fileName;
    }

    @Nonnull
    @CheckReturnValue
    public static String append(@Nonnull String fileName, @Nonnull FileType fileType) {
        final String extension = fileType.getDefaultExtension();
        if (fileName.endsWith(DELIMITER + extension)) {
            return fileName;
        }
        return fileName + DELIMITER + extension;
    }
}
