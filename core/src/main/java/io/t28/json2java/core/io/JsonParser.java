package io.t28.json2java.core.io;

import io.t28.json2java.core.io.exception.JsonParseException;
import io.t28.json2java.core.json.JsonValue;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface JsonParser {
    @Nonnull
    @CheckReturnValue
    JsonValue parse(@Nonnull String json) throws JsonParseException;
}
