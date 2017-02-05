package io.t28.pojojson.idea.ui;

import com.intellij.openapi.ui.InputValidatorEx;
import io.t28.pojojson.idea.Type;
import org.jetbrains.annotations.Nullable;

public class TypeValidator implements InputValidatorEx {
    @Nullable
    @Override
    public String getErrorText(@Nullable String type) {
        return "This(" + type + ") is not a supported type";
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
