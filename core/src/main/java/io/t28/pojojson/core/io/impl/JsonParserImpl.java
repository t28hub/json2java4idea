package io.t28.pojojson.core.io.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import io.t28.pojojson.core.io.JsonParser;
import io.t28.pojojson.core.io.exception.JsonParseException;
import io.t28.pojojson.core.json.JsonValue;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JsonParserImpl implements JsonParser {
    private final ObjectMapper mapper;

    public JsonParserImpl() {
        this(new ObjectMapper());
    }

    @VisibleForTesting
    JsonParserImpl(@Nonnull ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Nonnull
    @Override
    public JsonValue parse(@Nonnull String json) throws JsonParseException {
        try {
            final Object parsed = mapper.readValue(json, Object.class);
            return JsonValue.wrap(parsed);
        } catch (IOException e) {
            throw new JsonParseException("Unable to parse a JSON string(" + json + ")", e);
        }
    }
}
