package io.t28.pojojson.idea.validator;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.intellij.openapi.ui.InputValidatorEx;
import io.t28.pojojson.idea.PluginBundle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JsonValidator implements InputValidatorEx {
    private final PluginBundle bundle;
    private final JsonParser parser;

    @Inject
    public JsonValidator(@Nonnull PluginBundle bundle, @Nonnull JsonParser parser) {
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
                return bundle.message("new.message.validator.json.primitive");
            }
        } catch (JsonSyntaxException e) {
            return bundle.message("error.message.validator.json.invalid");
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
