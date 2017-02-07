package io.t28.pojojson.core.io;

import io.t28.pojojson.core.io.exception.JsonParseException;
import io.t28.pojojson.core.json.JsonValue;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface JsonParser {
    @Nonnull
    @CheckReturnValue
    JsonValue read(@Nonnull String json) throws JsonParseException;
}
