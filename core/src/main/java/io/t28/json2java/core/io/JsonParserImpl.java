/*
 * Copyright (c) 2017 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.t28.json2java.core.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import io.t28.json2java.core.io.exception.JsonParseException;
import io.t28.json2java.core.json.JsonValue;

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
