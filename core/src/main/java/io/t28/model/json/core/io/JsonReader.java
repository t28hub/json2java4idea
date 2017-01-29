package io.t28.model.json.core.io;

import io.t28.model.json.core.io.exception.JsonReadException;
import io.t28.model.json.core.json.JsonValue;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface JsonReader {
    @Nonnull
    @CheckReturnValue
    JsonValue read(@Nonnull String json) throws JsonReadException;
}
