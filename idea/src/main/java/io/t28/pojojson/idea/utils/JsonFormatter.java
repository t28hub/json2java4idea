package io.t28.pojojson.idea.utils;

import io.t28.pojojson.idea.exceptions.JsonFormatException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;

public interface JsonFormatter {
    @NotNull
    @CheckReturnValue
    String format(@NotNull String json) throws JsonFormatException;
}
