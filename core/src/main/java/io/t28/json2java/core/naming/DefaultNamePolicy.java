package io.t28.json2java.core.naming;

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
            return format(name, CaseFormat.UPPER_CAMEL);
        }
    },
    METHOD {
        private static final String BOOLEAN_PREFIX = "is";
        private static final String GENERAL_PREFIX = "get";

        @Nonnull
        @Override
        public String convert(@Nonnull String name, @Nonnull TypeName type) {
            final StringBuilder builder = new StringBuilder();
            if (type.equals(TypeName.BOOLEAN) || type.equals(ClassName.get(Boolean.class))) {
                builder.append(BOOLEAN_PREFIX);
            } else {
                builder.append(GENERAL_PREFIX);
            }
            return builder.append(format(name, CaseFormat.UPPER_CAMEL))
                    .toString();
        }
    },
    FIELD {
        @Nonnull
        @Override
        public String convert(@Nonnull String name, @Nonnull TypeName type) {
            return format(name, CaseFormat.LOWER_CAMEL);
        }
    },
    PARAMETER {
        @Nonnull
        @Override
        public String convert(@Nonnull String name, @Nonnull TypeName type) {
            return format(name, CaseFormat.LOWER_CAMEL);
        }
    };

    private static final String NO_TEXT = "";
    private static final String DELIMITER = "_";
    private static final String DELIMITER_REGEX = "(_|-|\\s|\\.|:)";
    private static final String CAMEL_CASE_REGEX = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
    private static final String INVALID_IDENTIFIER_REGEX = "[^A-Za-z0-9_$]";

    @Nonnull
    @CheckReturnValue
    public static String format(@Nonnull String name, @Nonnull CaseFormat format) {
        final Matcher matcher = Pattern.compile(DELIMITER_REGEX).matcher(name);
        final String pattern;
        if (matcher.find()) {
            pattern = DELIMITER_REGEX;
        } else {
            pattern = CAMEL_CASE_REGEX;
        }
        final String snakeCase = Stream.of(name.split(pattern))
                .map(Ascii::toLowerCase)
                .map(part -> part.replaceAll(INVALID_IDENTIFIER_REGEX, NO_TEXT))
                .filter(part -> !Strings.isNullOrEmpty(part))
                .collect(Collectors.joining(DELIMITER));
        return CaseFormat.LOWER_UNDERSCORE.to(format, snakeCase);
    }
}
