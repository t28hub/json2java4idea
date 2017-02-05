package io.t28.pojojson.idea.ui;

import com.intellij.openapi.ui.InputValidator;
import org.jetbrains.annotations.Nullable;

public class NullValidator implements InputValidator {
    @Override
    public boolean checkInput(@Nullable String text) {
        return false;
    }

    @Override
    public boolean canClose(@Nullable String text) {
        return false;
    }
}
