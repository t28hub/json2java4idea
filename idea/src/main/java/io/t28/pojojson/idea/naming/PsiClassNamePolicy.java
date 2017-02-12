package io.t28.pojojson.idea.naming;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.impl.PsiNameHelperImpl;
import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.DefaultNamePolicy;
import io.t28.pojojson.core.naming.NamePolicy;

import javax.annotation.Nonnull;

public class PsiClassNamePolicy implements NamePolicy {
    private static final String DELIMITER = "_";

    private final PsiNameHelper nameHelper;

    public PsiClassNamePolicy() {
        this(PsiNameHelperImpl.getInstance());
    }

    @VisibleForTesting
    PsiClassNamePolicy(@Nonnull PsiNameHelper nameHelper) {
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
