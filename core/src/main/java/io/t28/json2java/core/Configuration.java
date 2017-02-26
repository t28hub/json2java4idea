package io.t28.json2java.core;

import com.google.common.base.Preconditions;
import io.t28.json2java.core.builder.ClassBuilder;
import io.t28.json2java.core.builder.GsonClassBuilder;
import io.t28.json2java.core.builder.JacksonClassBuilder;
import io.t28.json2java.core.builder.ModelClassBuilder;
import io.t28.json2java.core.builder.MoshiClassBuilder;
import io.t28.json2java.core.io.JavaBuilder;
import io.t28.json2java.core.io.JavaBuilderImpl;
import io.t28.json2java.core.io.JsonParser;
import io.t28.json2java.core.io.JsonParserImpl;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.naming.NamePolicy;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class Configuration {
    private final Style style;
    private final NamePolicy classNamePolicy;
    private final NamePolicy fieldNamePolicy;
    private final NamePolicy methodNamePolicy;
    private final NamePolicy parameterNamePolicy;
    private final JsonParser jsonParser;
    private final JavaBuilder javaBuilder;

    private Configuration(@Nonnull Builder builder) {
        style = builder.style;
        classNamePolicy = builder.classNamePolicy;
        fieldNamePolicy = builder.fieldNamePolicy;
        methodNamePolicy = builder.methodNamePolicy;
        parameterNamePolicy = builder.parameterNamePolicy;
        jsonParser = builder.jsonParser;
        javaBuilder = builder.javaBuilder;
    }

    @Nonnull
    @CheckReturnValue
    public static Builder builder() {
        return new Builder();
    }

    @Nonnull
    @CheckReturnValue
    public Style style() {
        return style;
    }

    @Nonnull
    @CheckReturnValue
    public NamePolicy classNamePolicy() {
        return classNamePolicy;
    }

    @Nonnull
    @CheckReturnValue
    public NamePolicy fieldNamePolicy() {
        return fieldNamePolicy;
    }

    @Nonnull
    @CheckReturnValue
    public NamePolicy methodNamePolicy() {
        return methodNamePolicy;
    }

    @Nonnull
    @CheckReturnValue
    public NamePolicy parameterNamePolicy() {
        return parameterNamePolicy;
    }

    @Nonnull
    @CheckReturnValue
    public JsonParser jsonParser() {
        return jsonParser;
    }

    @Nonnull
    @CheckReturnValue
    public JavaBuilder javaBuilder() {
        return javaBuilder;
    }

    @Nonnull
    @CheckReturnValue
    public ClassBuilder classBuilder() {
        return style().toBuilder(
                fieldNamePolicy(),
                methodNamePolicy(),
                parameterNamePolicy()
        );
    }

    public static class Builder {
        private Style style;
        private NamePolicy classNamePolicy;
        private NamePolicy fieldNamePolicy;
        private NamePolicy methodNamePolicy;
        private NamePolicy parameterNamePolicy;
        private JsonParser jsonParser;
        private JavaBuilder javaBuilder;

        private Builder() {
            style = Style.NONE;
            classNamePolicy = DefaultNamePolicy.CLASS;
            fieldNamePolicy = DefaultNamePolicy.FIELD;
            methodNamePolicy = DefaultNamePolicy.METHOD;
            parameterNamePolicy = DefaultNamePolicy.PARAMETER;
            jsonParser = new JsonParserImpl();
            javaBuilder = new JavaBuilderImpl();
        }

        @Nonnull
        @CheckReturnValue
        public Builder style(@Nonnull Style style) {
            this.style = Preconditions.checkNotNull(style);
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder classNamePolicy(@Nonnull NamePolicy classNamePolicy) {
            this.classNamePolicy = Preconditions.checkNotNull(classNamePolicy);
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder fieldNamePolicy(@Nonnull NamePolicy fieldNamePolicy) {
            this.fieldNamePolicy = Preconditions.checkNotNull(fieldNamePolicy);
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder methodNamePolicy(@Nonnull NamePolicy methodNamePolicy) {
            this.methodNamePolicy = Preconditions.checkNotNull(methodNamePolicy);
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder parameterNamePolicy(@Nonnull NamePolicy parameterNamePolicy) {
            this.parameterNamePolicy = Preconditions.checkNotNull(parameterNamePolicy);
            return this;
        }

        @Nonnull
        @CheckReturnValue
        Builder jsonParser(@Nonnull JsonParser jsonParser) {
            this.jsonParser = Preconditions.checkNotNull(jsonParser);
            return this;
        }

        @Nonnull
        @CheckReturnValue
        Builder javaBuilder(@Nonnull JavaBuilder javaBuilder) {
            this.javaBuilder = Preconditions.checkNotNull(javaBuilder);
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Configuration build() {
            return new Configuration(this);
        }
    }

    public enum Style {
        NONE {
            @Nonnull
            @Override
            ClassBuilder toBuilder(@Nonnull NamePolicy fieldNameStrategy,
                                   @Nonnull NamePolicy methodNameStrategy,
                                   @Nonnull NamePolicy parameterNameStrategy) {
                return new ModelClassBuilder(fieldNameStrategy, methodNameStrategy, parameterNameStrategy);
            }
        },
        GSON {
            @Nonnull
            @Override
            ClassBuilder toBuilder(@Nonnull NamePolicy fieldNameStrategy,
                                   @Nonnull NamePolicy methodNameStrategy,
                                   @Nonnull NamePolicy parameterNameStrategy) {
                return new GsonClassBuilder(fieldNameStrategy, methodNameStrategy, parameterNameStrategy);
            }
        },
        JACKSON {
            @Nonnull
            @Override
            ClassBuilder toBuilder(@Nonnull NamePolicy fieldNameStrategy,
                                   @Nonnull NamePolicy methodNameStrategy,
                                   @Nonnull NamePolicy parameterNameStrategy) {
                return new JacksonClassBuilder(fieldNameStrategy, methodNameStrategy, parameterNameStrategy);
            }
        },
        MOSHI {
            @Nonnull
            @Override
            ClassBuilder toBuilder(@Nonnull NamePolicy fieldNameStrategy,
                                   @Nonnull NamePolicy methodNameStrategy,
                                   @Nonnull NamePolicy parameterNameStrategy) {
                return new MoshiClassBuilder(fieldNameStrategy, methodNameStrategy, parameterNameStrategy);
            }
        };

        @Nonnull
        @CheckReturnValue
        abstract ClassBuilder toBuilder(@Nonnull NamePolicy fieldNameStrategy,
                                        @Nonnull NamePolicy methodNameStrategy,
                                        @Nonnull NamePolicy parameterNameStrategy);

        @Nonnull
        @CheckReturnValue
        public static Optional<Style> fromName(@Nonnull String name) {
            return Stream.of(Style.values())
                    .filter(style -> style.name().equalsIgnoreCase(name))
                    .findFirst();
        }
    }
}
