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

package io.t28.json2java.idea.validator;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.intellij.openapi.ui.InputValidatorEx;
import io.t28.json2java.idea.Json2JavaBundle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JsonValidator implements InputValidatorEx {
    private final Json2JavaBundle bundle;
    private final JsonParser parser;

    @Inject
    public JsonValidator(@Nonnull Json2JavaBundle bundle, @Nonnull JsonParser parser) {
        this.bundle = bundle;
        this.parser = parser;
    }

    @Nullable
    @Override
    public String getErrorText(@Nullable String json) {
        if (Strings.isNullOrEmpty(json)) {
            return bundle.message("error.message.validator.json.empty");
        }

        try {
            final JsonElement root = parser.parse(json);
            if (root.isJsonNull() || root.isJsonPrimitive()) {
                return bundle.message("error.message.validator.json.primitive");
            }
        } catch (JsonSyntaxException e) {
            return bundle.message("error.message.validator.json.invalid");
        } catch (JsonParseException e) {
            return bundle.message("error.message.validator.json.parse");
        }
        return null;
    }

    @Override
    public boolean checkInput(@Nullable String json) {
        return true;
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean canClose(@Nullable String json) {
        if (Strings.isNullOrEmpty(json)) {
            return false;
        }

        try {
            final JsonElement root = parser.parse(json);
            return !root.isJsonNull() && !root.isJsonPrimitive();
        } catch (JsonParseException e) {
            return false;
        }
    }
}
