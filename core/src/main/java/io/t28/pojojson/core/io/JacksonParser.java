package io.t28.pojojson.core.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.t28.pojojson.core.io.exception.JsonParseException;
import io.t28.pojojson.core.json.JsonValue;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JacksonParser implements JsonParser {
    private final ObjectMapper mapper;

    public JacksonParser() {
        this(new ObjectMapper());
    }

    JacksonParser(@Nonnull ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Nonnull
    @Override
    public JsonValue read(@Nonnull String json) throws JsonParseException {
        try {
            final Object parsed = mapper.readValue(json, Object.class);
            return JsonValue.wrap(parsed);
        } catch (IOException e) {
            throw new JsonParseException("Unable to read a JSON string(" + json + ")", e);
        }
    }
}
