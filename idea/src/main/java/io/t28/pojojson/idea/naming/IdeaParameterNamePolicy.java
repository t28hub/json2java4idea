package io.t28.pojojson.idea.naming;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CaseFormat;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.psi.impl.PsiNameHelperImpl;
import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.DefaultNamePolicy;
import io.t28.pojojson.core.naming.NamePolicy;

import javax.annotation.Nonnull;

public class IdeaParameterNamePolicy implements NamePolicy {
    private static final String PREFIX = "_";

    private final PsiNameHelper nameHelper;
    private final JavaCodeStyleManager codeStyleManager;

    public IdeaParameterNamePolicy(@Nonnull Project project) {
        this(PsiNameHelperImpl.getInstance(project), JavaCodeStyleManager.getInstance(project));
    }

    @VisibleForTesting
    IdeaParameterNamePolicy(@Nonnull PsiNameHelper nameHelper, @Nonnull JavaCodeStyleManager codeStyleManager) {
        this.nameHelper = nameHelper;
        this.codeStyleManager = codeStyleManager;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String propertyName = DefaultNamePolicy.format(name, CaseFormat.LOWER_CAMEL);
        final String parameterName = codeStyleManager.propertyNameToVariableName(propertyName, VariableKind.PARAMETER);
        if (nameHelper.isKeyword(parameterName)) {
            return PREFIX + parameterName;
        }
        return parameterName;
    }
}
