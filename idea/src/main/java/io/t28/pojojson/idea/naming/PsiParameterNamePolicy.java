package io.t28.pojojson.idea.naming;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.impl.PsiNameHelperImpl;
import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.DefaultNamePolicy;
import io.t28.pojojson.core.naming.NamePolicy;

import javax.annotation.Nonnull;

public class PsiParameterNamePolicy implements NamePolicy {
    private static final String PREFIX = "_";

    private final PsiNameHelper nameHelper;

    public PsiParameterNamePolicy() {
        this(PsiNameHelperImpl.getInstance());
    }

    @VisibleForTesting
    PsiParameterNamePolicy(@Nonnull PsiNameHelper nameHelper) {
        this.nameHelper = nameHelper;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String fieldName = DefaultNamePolicy.FIELD.convert(name, type);
        if (nameHelper.isKeyword(fieldName)) {
            return PREFIX + fieldName;
        }
        return fieldName;
    }
}
