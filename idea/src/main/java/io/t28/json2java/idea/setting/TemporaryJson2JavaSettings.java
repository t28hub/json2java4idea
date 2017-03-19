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

package io.t28.json2java.idea.setting;

import io.t28.json2java.core.Style;

import javax.annotation.Nonnull;

public class TemporaryJson2JavaSettings implements Json2JavaSettings {
    @Nonnull
    private Style style;
    @Nonnull
    private String classNamePrefix;
    @Nonnull
    private String classNameSuffix;

    TemporaryJson2JavaSettings() {
        style = Style.NONE;
        classNamePrefix = "";
        classNameSuffix = "";
    }

    @Nonnull
    @Override
    public Style getStyle() {
        return style;
    }

    @Nonnull
    @Override
    public TemporaryJson2JavaSettings setStyle(@Nonnull Style style) {
        this.style = style;
        return this;
    }

    @Nonnull
    @Override
    public String getClassNamePrefix() {
        return classNamePrefix;
    }

    @Nonnull
    @Override
    public TemporaryJson2JavaSettings setClassNamePrefix(@Nonnull String classNamePrefix) {
        this.classNamePrefix = classNamePrefix;
        return this;
    }

    @Nonnull
    @Override
    public String getClassNameSuffix() {
        return classNameSuffix;
    }

    @Nonnull
    @Override
    public TemporaryJson2JavaSettings setClassNameSuffix(@Nonnull String classNameSuffix) {
        this.classNameSuffix = classNameSuffix;
        return this;
    }
}
