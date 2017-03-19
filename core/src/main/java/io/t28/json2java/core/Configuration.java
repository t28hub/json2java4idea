/*
 * Copyright (c) 2017 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.t28.json2java.core;

import com.google.common.base.Preconditions;
import io.t28.json2java.core.builder.ClassBuilder;
import io.t28.json2java.core.io.JavaBuilder;
import io.t28.json2java.core.io.JavaBuilderImpl;
import io.t28.json2java.core.io.JsonParser;
import io.t28.json2java.core.io.JsonParserImpl;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.naming.NamePolicy;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

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

}
