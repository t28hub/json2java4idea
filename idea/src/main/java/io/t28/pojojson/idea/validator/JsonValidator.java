package io.t28.pojojson.idea.validator;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.intellij.openapi.ui.InputValidatorEx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JsonValidator implements InputValidatorEx {
    private final JsonParser parser;

    @Inject
    public JsonValidator(@Nonnull JsonParser parser) {
        this.parser = parser;
    }

    @Nullable
    @Override
    public String getErrorText(@Nullable String json) {
        if (Strings.isNullOrEmpty(json)) {
            return "JSON must not be empty";
        }

        try {
            final JsonElement root = parser.parse(json);
            if (root.isJsonNull() || root.isJsonPrimitive()) {
                return "JSON must be an Array or Object";
            }
        } catch (JsonSyntaxException e) {
            return "This is not a valid JSON string";
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
            return root.isJsonArray() || root.isJsonObject();
        } catch (JsonSyntaxException e) {
            return false;
        }
    }
}
