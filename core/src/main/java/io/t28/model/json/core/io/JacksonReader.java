package io.t28.model.json.core.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.t28.model.json.core.io.exception.JsonReadException;
import io.t28.model.json.core.json.JsonValue;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JacksonReader implements JsonReader {
    private final ObjectMapper mapper;

    public JacksonReader() {
        this(new ObjectMapper());
    }

    JacksonReader(@Nonnull ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Nonnull
    @Override
    public JsonValue read(@Nonnull String json) throws JsonReadException {
        try {
            final Object parsed = mapper.readValue(json, Object.class);
            return JsonValue.wrap(parsed);
        } catch (IOException e) {
            throw new JsonReadException("Unable to read a JSON string(" + json + ")", e);
        }
    }
}
