package io.t28.pojojson.idea.naming;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CaseFormat;
import com.intellij.codeInsight.generation.GenerateMembersUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.PsiNameHelperImpl;
import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.DefaultNamePolicy;
import io.t28.pojojson.core.naming.NamePolicy;
import io.t28.pojojson.idea.utils.PsiTypes;

import javax.annotation.Nonnull;

public class MethodNamePolicy implements NamePolicy {
    private final Project project;
    private final PsiNameHelper nameHelper;

    public MethodNamePolicy(@Nonnull Project project) {
        this(project, PsiNameHelperImpl.getInstance(project));
    }

    @VisibleForTesting
    MethodNamePolicy(@Nonnull Project project, @Nonnull PsiNameHelper nameHelper) {
        this.project = project;
        this.nameHelper = nameHelper;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String variableName = DefaultNamePolicy.format(name, CaseFormat.LOWER_CAMEL);
        final PsiType psiType = new PsiTypes(project).apply(type);
        final String methodName = GenerateMembersUtil.suggestGetterName(variableName, psiType, project);
        if (nameHelper.isKeyword(methodName)) {
            throw new IllegalArgumentException("Generated method name is reserved by Java");
        }
        return methodName;
    }
}
