package io.t28.pojojson.idea.naming;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.intellij.codeInsight.generation.GenerateMembersUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.PsiType;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.naming.NamePolicy;
import io.t28.pojojson.idea.utils.PsiTypeConverter;

import javax.annotation.Nonnull;

public class MethodNamePolicy implements NamePolicy {
    private final Project project;
    private final PsiNameHelper nameHelper;
    private final PsiTypeConverter typeConverter;

    @Inject
    public MethodNamePolicy(@Nonnull Project project,
                            @Nonnull PsiNameHelper nameHelper,
                            @Nonnull PsiTypeConverter typeConverter) {
        this.project = Preconditions.checkNotNull(project);
        this.nameHelper = Preconditions.checkNotNull(nameHelper);
        this.typeConverter = Preconditions.checkNotNull(typeConverter);
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String variableName = DefaultNamePolicy.format(name, CaseFormat.LOWER_CAMEL);
        final PsiType psiType = typeConverter.apply(type);
        final String methodName = GenerateMembersUtil.suggestGetterName(variableName, psiType, project);
        if (nameHelper.isKeyword(methodName)) {
            throw new IllegalArgumentException("Generated method name is reserved by Java");
        }
        return methodName;
    }
}
