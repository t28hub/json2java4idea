package io.t28.model.json.core.naming;

import com.google.common.base.Strings;
import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DefaultNamingStrategy implements NamingStrategy {
    private final NamingCase namingCase;
    private final String prefix;
    private final String suffix;

    public DefaultNamingStrategy(@Nonnull NamingCase namingCase, @Nullable String prefix, @Nullable String suffix) {
        this.namingCase = namingCase;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Nonnull
    @Override
    public String apply(@Nonnull TypeName type, @Nonnull String name, @Nonnull NamingCase nameCase) {
        final StringBuilder builder = new StringBuilder(name.length());
        if (!Strings.isNullOrEmpty(prefix)) {
            builder.append(prefix).append(NamingCase.KEBAB_CASE_DELIMITER);
        }

        builder.append(nameCase.to(NamingCase.UPPER_KEBAB_CASE, name));

        if (!Strings.isNullOrEmpty(suffix)) {
            builder.append(NamingCase.KEBAB_CASE_DELIMITER).append(suffix);
        }
        return NamingCase.UPPER_KEBAB_CASE.to(namingCase, builder.toString());
    }
}
