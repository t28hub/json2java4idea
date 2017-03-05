package io.t28.json2java.idea.validator;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.psi.PsiNameHelper;
import io.t28.json2java.idea.Json2JavaBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClassSuffixValidator implements InputValidatorEx {
    private final Json2JavaBundle bundle;
    private final PsiNameHelper nameHelper;

    @Inject
    public ClassSuffixValidator(@NotNull Json2JavaBundle bundle, @NotNull PsiNameHelper nameHelper) {
        this.bundle = bundle;
        this.nameHelper = nameHelper;
    }

    @Nullable
    @Override
    public String getErrorText(@Nullable String suffix) {
        if (canClose(suffix)) {
            return null;
        }
        return bundle.message("error.message.validator.class.suffix", suffix);
    }

    @Override
    public boolean checkInput(@Nullable String suffix) {
        return true;
    }

    @Override
    public boolean canClose(@Nullable String suffix) {
        if (Strings.isNullOrEmpty(suffix)) {
            return true;
        }

        return nameHelper.isIdentifier(suffix);
    }
}
