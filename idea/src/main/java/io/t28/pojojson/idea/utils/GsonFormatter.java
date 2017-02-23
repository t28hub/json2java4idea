package io.t28.pojojson.idea.utils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.gson.*;
import io.t28.pojojson.idea.exceptions.InvalidJsonException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GsonFormatter implements JsonFormatter {
    private static final String EMPTY_JSON = "";

    private final Gson gson;
    private final JsonParser parser;

    public GsonFormatter() {
        this(new GsonBuilder().setPrettyPrinting().serializeNulls().create(), new JsonParser());
    }

    @VisibleForTesting
    GsonFormatter(@NotNull Gson gson, @NotNull JsonParser parser) {
        this.gson = gson;
        this.parser = parser;
    }

    @NotNull
    @Override
    public String format(@NotNull String json) throws InvalidJsonException, IOException {
        final String trimmed = json.trim();
        if (Strings.isNullOrEmpty(trimmed)) {
            return EMPTY_JSON;
        }

        try {
            final JsonElement tree = parser.parse(trimmed);
            if (tree.isJsonNull() || tree.isJsonPrimitive()) {
                return trimmed;
            }
            return gson.toJson(tree);
        } catch (JsonSyntaxException e) {
            throw new InvalidJsonException("JSON string has syntax error", e);
        } catch (JsonIOException e) {
            throw new IOException("I/O error occurred during writing", e);
        }
    }
}
