package io.t28.pojojson.idea.naming;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.intellij.psi.PsiNameHelper;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.naming.NamePolicy;
import io.t28.pojojson.idea.settings.Json2JavaSettings;

import javax.annotation.Nonnull;

public class ClassNamePolicy implements NamePolicy {
    private final PsiNameHelper nameHelper;
    private final String prefix;
    private final String suffix;

    @Inject
    public ClassNamePolicy(@Nonnull PsiNameHelper nameHelper, @Nonnull Json2JavaSettings settings) {
        this(nameHelper, settings.getClassNamePrefix(), settings.getClassNameSuffix());
    }

    @VisibleForTesting
    ClassNamePolicy(@Nonnull PsiNameHelper nameHelper, @Nonnull String prefix, @Nonnull String suffix) {
        this.nameHelper = nameHelper;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final StringBuilder builder = new StringBuilder();
        builder.append(prefix)
                .append(DefaultNamePolicy.CLASS.convert(name, type))
                .append(suffix);
        final String className = builder.toString();
        if (!nameHelper.isQualifiedName(className)) {
            throw new IllegalArgumentException();
        }
        return className;
    }
}
