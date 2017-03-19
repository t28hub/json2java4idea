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

package io.t28.json2java.core.json;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class JsonArray extends JsonValue {
    private final List<Object> value;

    public JsonArray(@Nonnull List<Object> value) {
        this.value = new LinkedList<>(value);
    }

    @Nonnull
    @Override
    public TypeName getType() {
        return ParameterizedTypeName.get(List.class, Object.class);
    }

    @Nonnull
    @Override
    public List<Object> getValue() {
        return ImmutableList.copyOf(value);
    }

    @Nonnull
    @CheckReturnValue
    public Stream<JsonValue> stream() {
        return value.stream().map(JsonValue::wrap);
    }
}
