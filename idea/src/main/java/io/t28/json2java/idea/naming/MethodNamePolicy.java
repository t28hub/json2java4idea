package io.t28.json2java.idea.naming;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.intellij.codeInsight.generation.GenerateMembersUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiType;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.naming.NamePolicy;
import io.t28.json2java.idea.util.PsiTypeConverter;

import javax.annotation.Nonnull;

public class MethodNamePolicy implements NamePolicy {
    private final Project project;
    private final PsiTypeConverter typeConverter;

    @Inject
    public MethodNamePolicy(@Nonnull Project project, @Nonnull PsiTypeConverter typeConverter) {
        this.project = Preconditions.checkNotNull(project);
        this.typeConverter = Preconditions.checkNotNull(typeConverter);
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String variableName = DefaultNamePolicy.format(name, CaseFormat.LOWER_CAMEL);
        if (Strings.isNullOrEmpty(variableName)) {
            throw new IllegalArgumentException("Cannot convert '" + name + "' to a method name");
        }

        final PsiType psiType = typeConverter.apply(type);
        return GenerateMembersUtil.suggestGetterName(variableName, psiType, project);
    }
}
