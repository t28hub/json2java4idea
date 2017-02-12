package io.t28.pojojson.core.naming;

import com.google.common.base.Ascii;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DefaultNamePolicy implements NamePolicy {
    CLASS {
        @Nonnull
        @Override
        public String convert(@Nonnull String name, @Nonnull TypeName type) {
            final String snakeCase = normalize(name, DELIMITER);
            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, snakeCase);
        }
    },
    FIELD {
        @Nonnull
        @Override
        public String convert(@Nonnull String name, @Nonnull TypeName type) {
            final String snakeCase = normalize(name, DELIMITER);
            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, snakeCase);
        }
    },
    METHOD {
        private static final String BOOLEAN_PREFIX = "is";
        private static final String GENERAL_PREFIX = "get";

        @Nonnull
        @Override
        public String convert(@Nonnull String name, @Nonnull TypeName type) {
            final StringBuilder builder = new StringBuilder();
            if (type.equals(TypeName.BOOLEAN) || type.equals(ClassName.get(Boolean.TYPE))) {
                builder.append(BOOLEAN_PREFIX);
            } else {
                builder.append(GENERAL_PREFIX);
            }

            final String snakeCase = normalize(name, DELIMITER);
            final String upperCase = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, snakeCase);
            builder.append(upperCase);
            return builder.toString();
        }
    },
    PARAMETER {
        @Nonnull
        @Override
        public String convert(@Nonnull String name, @Nonnull TypeName type) {
            final String snakeCase = normalize(name, DELIMITER);
            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, snakeCase);
        }
    };

    private static final String NO_TEXT = "";
    private static final String DELIMITER = "_";
    private static final String DELIMITER_REGEX = "(_|-|\\s|\\.|:)";
    private static final String CAMEL_CASE_REGEX = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
    private static final String INVALID_IDENTIFIER_REGEX = "[^A-Za-z0-9_$]";

    @Nonnull
    @CheckReturnValue
    public static String normalize(@Nonnull String name, @Nonnull String delimiter) {
        final Matcher matcher = Pattern.compile(DELIMITER_REGEX).matcher(name);
        final String pattern;
        if (matcher.matches()) {
            pattern = DELIMITER_REGEX;
        } else {
            pattern = CAMEL_CASE_REGEX;
        }
        return Stream.of(name.split(pattern))
                .map(Ascii::toLowerCase)
                .map(part -> part.replaceAll(INVALID_IDENTIFIER_REGEX, NO_TEXT))
                .filter(part -> !Strings.isNullOrEmpty(part))
                .collect(Collectors.joining(delimiter));
    }
}
