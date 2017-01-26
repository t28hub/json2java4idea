package io.t28.model.json.core.parser;

import io.t28.model.json.core.json.JsonValue;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface JsonParser {
    @Nonnull
    @CheckReturnValue
    JsonValue parse(@Nonnull String json) throws JsonParseException;
}
