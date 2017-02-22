package io.t28.pojojson.idea.validator;

import com.google.common.base.Strings;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.psi.PsiNameHelper;
import io.t28.pojojson.idea.PluginBundle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

public class NameValidator implements InputValidatorEx {
    private final PluginBundle bundle;
    private final PsiNameHelper nameHelper;

    @Inject
    public NameValidator(@Nonnull PluginBundle bundle, @Nonnull PsiNameHelper nameHelper) {
        this.bundle = bundle;
        this.nameHelper = nameHelper;
    }

    @Nullable
    @Override
    public String getErrorText(@Nullable String name) {
        return bundle.message("error.message.validator.name.invalid");
    }

    @Override
    public boolean checkInput(@Nullable String name) {
        return true;
    }

    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean canClose(@Nullable String name) {
        if (Strings.isNullOrEmpty(name)) {
            return false;
        }
        return nameHelper.isQualifiedName(name);
    }
}
