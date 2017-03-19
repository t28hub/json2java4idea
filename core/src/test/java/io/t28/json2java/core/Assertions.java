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

package io.t28.json2java.core;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.FieldSpecAssert;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpecAssert;
import io.t28.json2java.core.json.JsonValue;
import io.t28.json2java.core.json.JsonValueAssert;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Assertions extends org.assertj.core.api.Assertions {
    private Assertions() {
    }

    @Nonnull
    @CheckReturnValue
    public static JsonValueAssert assertThat(@Nullable JsonValue actual) {
        return new JsonValueAssert(actual);
    }

    @Nonnull
    @CheckReturnValue
    public static FieldSpecAssert assertThat(@Nullable FieldSpec actual) {
        return new FieldSpecAssert(actual);
    }

    @Nonnull
    @CheckReturnValue
    public static MethodSpecAssert assertThat(@Nullable MethodSpec actual) {
        return new MethodSpecAssert(actual);
    }
}
