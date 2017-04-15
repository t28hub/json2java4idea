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
import com.google.common.collect.ImmutableSet;
import io.t28.json2java.core.annotation.AnnotationPolicy;
import io.t28.json2java.core.builder.ClassBuilder;
import io.t28.json2java.core.io.JavaBuilder;
import io.t28.json2java.core.io.JavaBuilderImpl;
import io.t28.json2java.core.io.JsonParser;
import io.t28.json2java.core.io.JsonParserImpl;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.naming.NamePolicy;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class Configuration {
    private final Style style;
    private final NamePolicy classNamePolicy;
    private final NamePolicy fieldNamePolicy;
    private final NamePolicy methodNamePolicy;
    private final NamePolicy parameterNamePolicy;
    private final Set<AnnotationPolicy> annotationPolicies;
    private final JsonParser jsonParser;
    private final JavaBuilder javaBuilder;

    private Configuration(@Nonnull Builder builder) {
        style = builder.style;
        classNamePolicy = builder.classNamePolicy;
        fieldNamePolicy = builder.fieldNamePolicy;
        methodNamePolicy = builder.methodNamePolicy;
        parameterNamePolicy = builder.parameterNamePolicy;
        annotationPolicies = ImmutableSet.copyOf(builder.annotationPolicies);
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
    public Set<AnnotationPolicy> annotationPolicies() {
        return ImmutableSet.copyOf(annotationPolicies);
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
        private Set<AnnotationPolicy> annotationPolicies;
        private JsonParser jsonParser;
        private JavaBuilder javaBuilder;

        private Builder() {
            style = Style.NONE;
            classNamePolicy = DefaultNamePolicy.CLASS;
            fieldNamePolicy = DefaultNamePolicy.FIELD;
            methodNamePolicy = DefaultNamePolicy.METHOD;
            parameterNamePolicy = DefaultNamePolicy.PARAMETER;
            annotationPolicies = new LinkedHashSet<>();
            jsonParser = new JsonParserImpl();
            javaBuilder = new JavaBuilderImpl();
        }

        @Nonnull
        public Builder style(@Nonnull Style style) {
            this.style = Preconditions.checkNotNull(style);
            return this;
        }

        @Nonnull
        public Builder classNamePolicy(@Nonnull NamePolicy classNamePolicy) {
            this.classNamePolicy = Preconditions.checkNotNull(classNamePolicy);
            return this;
        }

        @Nonnull
        public Builder fieldNamePolicy(@Nonnull NamePolicy fieldNamePolicy) {
            this.fieldNamePolicy = Preconditions.checkNotNull(fieldNamePolicy);
            return this;
        }

        @Nonnull
        public Builder methodNamePolicy(@Nonnull NamePolicy methodNamePolicy) {
            this.methodNamePolicy = Preconditions.checkNotNull(methodNamePolicy);
            return this;
        }

        @Nonnull
        public Builder parameterNamePolicy(@Nonnull NamePolicy parameterNamePolicy) {
            this.parameterNamePolicy = Preconditions.checkNotNull(parameterNamePolicy);
            return this;
        }

        @Nonnull
        public Builder annotationPolicy(@Nonnull AnnotationPolicy annotationPolicy) {
            this.annotationPolicies.add(annotationPolicy);
            return this;
        }

        @Nonnull
        Builder jsonParser(@Nonnull JsonParser jsonParser) {
            this.jsonParser = Preconditions.checkNotNull(jsonParser);
            return this;
        }

        @Nonnull
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
