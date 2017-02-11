package io.t28.pojojson.idea.naming;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.impl.PsiNameHelperImpl;
import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.DefaultNamePolicy;
import io.t28.pojojson.core.naming.NamePolicy;

import javax.annotation.Nonnull;

public class PsiMethodNamePolicy implements NamePolicy {
    private final PsiNameHelper nameHelper;

    public PsiMethodNamePolicy() {
        this(PsiNameHelperImpl.getInstance());
    }

    @VisibleForTesting
    PsiMethodNamePolicy(@Nonnull PsiNameHelper nameHelper) {
        this.nameHelper = nameHelper;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String methodName = DefaultNamePolicy.METHOD.convert(name, type);
        if (nameHelper.isKeyword(methodName)) {
            throw new IllegalArgumentException("Generated method name is reserved by Java");
        }
        return methodName;
    }
}
