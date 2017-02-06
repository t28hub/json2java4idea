package io.t28.pojojson.idea.utils;

import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;
import java.io.IOException;

public interface JsonFormatter {
    @NotNull
    @CheckReturnValue
    String format(@NotNull String json) throws IOException;
}
