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

import io.t28.json2java.core.builder.ClassBuilder;
import io.t28.json2java.core.builder.GsonClassBuilder;
import io.t28.json2java.core.builder.JacksonClassBuilder;
import io.t28.json2java.core.builder.ModelClassBuilder;
import io.t28.json2java.core.builder.MoshiClassBuilder;
import io.t28.json2java.core.naming.NamePolicy;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

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

    @Nonnull
    @CheckReturnValue
    public static Style fromName(@Nonnull String name, @Nonnull Style defaultStyle) {
        return fromName(name).orElse(defaultStyle);
    }

    public String get() {
        return "";
    }
}
