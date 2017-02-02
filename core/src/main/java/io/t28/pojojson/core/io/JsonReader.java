package io.t28.pojojson.core.io;

import io.t28.pojojson.core.io.exception.JsonReadException;
import io.t28.pojojson.core.json.JsonValue;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface JsonReader {
    @Nonnull
    @CheckReturnValue
    JsonValue read(@Nonnull String json) throws JsonReadException;
}
