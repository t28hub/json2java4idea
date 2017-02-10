package io.t28.pojojson.core.naming.defaults;

import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.CasePolicy;
import io.t28.pojojson.core.naming.NamePolicy;

import javax.annotation.Nonnull;

public class ClassNamePolicy implements NamePolicy {
    private final CasePolicy nameCase;

    public ClassNamePolicy(@Nonnull CasePolicy nameCase) {
        this.nameCase = nameCase;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        return nameCase.convert(CasePolicy.UPPER_CAMEL_CASE, name);
    }
}
