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

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import io.t28.json2java.core.Style;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface Json2JavaSettings {
    @Nonnull
    @CheckReturnValue
    static Json2JavaSettings getInstance() {
        return new TemporaryJson2JavaSettings();
    }

    @Nonnull
    @CheckReturnValue
    static Json2JavaSettings getInstance(@Nonnull Project project) {
        return ServiceManager.getService(project, Json2JavaSettings.class);
    }

    @Nonnull
    @CheckReturnValue
    Style getStyle();

    @Nonnull
    Json2JavaSettings setStyle(@Nonnull Style style);

    @Nonnull
    @CheckReturnValue
    String getClassNamePrefix();

    @Nonnull
    Json2JavaSettings setClassNamePrefix(@Nonnull String classNamePrefix);

    @Nonnull
    @CheckReturnValue
    String getClassNameSuffix();

    @Nonnull
    Json2JavaSettings setClassNameSuffix(@Nonnull String classNameSuffix);

    @CheckReturnValue
    boolean isGeneratedAnnotationEnabled();

    @Nonnull
    Json2JavaSettings setGeneratedAnnotationEnabled(boolean enabled);

    @CheckReturnValue
    boolean isSuppressWarningsAnnotationEnabled();

    @Nonnull
    Json2JavaSettings setSuppressWarningsAnnotationEnabled(boolean enabled);
}
