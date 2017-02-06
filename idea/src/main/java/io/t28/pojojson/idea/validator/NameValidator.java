package io.t28.pojojson.idea.validator;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.psi.PsiNameHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NameValidator implements InputValidatorEx {
    private final PsiNameHelper nameHelper;

    public NameValidator(@NotNull Project project) {
        this(PsiNameHelper.getInstance(project));
    }

    @VisibleForTesting
    NameValidator(@NotNull PsiNameHelper nameHelper) {
        this.nameHelper = nameHelper;
    }

    @Nullable
    @Override
    public String getErrorText(@Nullable String name) {
        return "This is not a valid Java class name";
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
        if (!nameHelper.isQualifiedName(name)) {
            return false;
        }
        return true;
    }
}
