package io.t28.pojojson.idea.validator;

import com.google.inject.Inject;
import com.intellij.openapi.ui.InputValidatorEx;
import io.t28.pojojson.idea.PluginBundle;
import io.t28.pojojson.idea.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TypeValidator implements InputValidatorEx {
    private final PluginBundle bundle;

    @Inject
    public TypeValidator(@Nonnull PluginBundle bundle) {
        this.bundle = bundle;
    }

    @Nullable
    @Override
    public String getErrorText(@Nullable String type) {
        if (canClose(type)) {
            return null;
        }
        return bundle.message("error.message.validator.type.unsupported");
    }

    @Override
    public boolean checkInput(@Nullable String type) {
        return true;
    }

    @Override
    public boolean canClose(@Nullable String type) {
        try {
            Type.fromName(type);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
