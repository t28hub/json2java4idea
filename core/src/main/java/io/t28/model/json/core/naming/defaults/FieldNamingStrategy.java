package io.t28.model.json.core.naming.defaults;

import com.google.common.base.Strings;
import com.squareup.javapoet.TypeName;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.NamingStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FieldNamingStrategy implements NamingStrategy {
    private final String prefix;
    private final String suffix;

    public FieldNamingStrategy() {
        this.prefix = null;
        this.suffix = null;
    }

    public FieldNamingStrategy(@Nullable String prefix, @Nullable String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Nonnull
    @Override
    public String transform(@Nonnull TypeName type, @Nonnull String name, @Nonnull NamingCase nameCase) {
        final StringBuilder builder = new StringBuilder(name.length());
        if (!Strings.isNullOrEmpty(prefix)) {
            builder.append(prefix).append(NamingCase.KEBAB_CASE_DELIMITER);
        }

        builder.append(nameCase.to(NamingCase.UPPER_KEBAB_CASE, name));

        if (!Strings.isNullOrEmpty(suffix)) {
            builder.append(NamingCase.KEBAB_CASE_DELIMITER).append(suffix);
        }
        return NamingCase.UPPER_KEBAB_CASE.to(NamingCase.LOWER_CAMEL_CASE, builder.toString());
    }
}
