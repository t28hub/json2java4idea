package io.t28.pojojson.idea.naming;

import com.google.inject.Inject;
import com.intellij.psi.PsiNameHelper;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.naming.NamePolicy;

import javax.annotation.Nonnull;

public class ClassNamePolicy implements NamePolicy {
    private final PsiNameHelper nameHelper;

    @Inject
    public ClassNamePolicy(@Nonnull PsiNameHelper nameHelper) {
        this.nameHelper = nameHelper;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String className = DefaultNamePolicy.CLASS.convert(name, type);
        if (!nameHelper.isQualifiedName(className)) {
            throw new IllegalArgumentException();
        }
        return className;
    }
}
