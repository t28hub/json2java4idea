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

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import io.t28.json2java.core.Style;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

@State(
        name = "Json2JavaSettings",
        storages = @Storage(value = "$PROJECT_CONFIG_DIR$/json2java.xml")
)
public class PersistentJson2JavaSettings implements Json2JavaSettings, PersistentStateComponent<Element> {
    private static final String NO_TEXT = "";
    private static final String ENABLED = "true";
    private static final String ROOT_ELEMENT = "component";
    private static final String CLASS_STYLE_ATTRIBUTE = "style";
    private static final String CLASS_NAME_PREFIX_ATTRIBUTE = "classNamePrefix";
    private static final String CLASS_NAME_SUFFIX_ATTRIBUTE = "classNameSuffix";
    private static final String ANNOTATION_GENERATED_ENABLED = "annotationGenerated";
    private static final String ANNOTATION_SUPPRESS_WARNINGS_ENABLED = "annotationSuppressWarnings";

    private Style style;
    private String classNamePrefix;
    private String classNameSuffix;
    private boolean isGeneratedAnnotationEnabled;
    private boolean isSuppressWarningsAnnotationEnabled;

    public PersistentJson2JavaSettings() {
        style = Style.NONE;
        classNamePrefix = NO_TEXT;
        classNameSuffix = NO_TEXT;
    }

    @Nullable
    @Override
    public Element getState() {
        final Element state = new Element(ROOT_ELEMENT);
        state.setAttribute(CLASS_STYLE_ATTRIBUTE, style.name());
        state.setAttribute(CLASS_NAME_PREFIX_ATTRIBUTE, classNamePrefix);
        state.setAttribute(CLASS_NAME_SUFFIX_ATTRIBUTE, classNameSuffix);
        state.setAttribute(ANNOTATION_GENERATED_ENABLED, String.valueOf(isGeneratedAnnotationEnabled));
        state.setAttribute(ANNOTATION_SUPPRESS_WARNINGS_ENABLED, String.valueOf(isSuppressWarningsAnnotationEnabled));
        return state;
    }

    @Override
    public void loadState(@Nonnull Element state) {
        style = Style.fromName(state.getAttributeValue(CLASS_STYLE_ATTRIBUTE), Style.NONE);
        classNamePrefix = state.getAttributeValue(CLASS_NAME_PREFIX_ATTRIBUTE, NO_TEXT);
        classNameSuffix = state.getAttributeValue(CLASS_NAME_SUFFIX_ATTRIBUTE, NO_TEXT);
        isGeneratedAnnotationEnabled = Boolean.valueOf(state.getAttributeValue(ANNOTATION_GENERATED_ENABLED, ENABLED));
        isSuppressWarningsAnnotationEnabled = Boolean.valueOf(state.getAttributeValue(ANNOTATION_SUPPRESS_WARNINGS_ENABLED, ENABLED));
    }

    @Override
    public void noStateLoaded() {
        style = Style.NONE;
        classNamePrefix = NO_TEXT;
        classNameSuffix = NO_TEXT;
        isGeneratedAnnotationEnabled = true;
        isSuppressWarningsAnnotationEnabled = true;
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public Style getStyle() {
        return style;
    }

    @Nonnull
    @Override
    public PersistentJson2JavaSettings setStyle(@Nonnull Style classStyle) {
        this.style = classStyle;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public String getClassNamePrefix() {
        return classNamePrefix;
    }

    @Nonnull
    @Override
    public PersistentJson2JavaSettings setClassNamePrefix(@Nonnull String classNamePrefix) {
        this.classNamePrefix = classNamePrefix;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public String getClassNameSuffix() {
        return classNameSuffix;
    }

    @Nonnull
    @Override
    public PersistentJson2JavaSettings setClassNameSuffix(@Nonnull String classNameSuffix) {
        this.classNameSuffix = classNameSuffix;
        return this;
    }

    @Override
    public boolean isGeneratedAnnotationEnabled() {
        return isGeneratedAnnotationEnabled;
    }

    @Nonnull
    @Override
    public Json2JavaSettings setGeneratedAnnotationEnabled(boolean enabled) {
        isGeneratedAnnotationEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSuppressWarningsAnnotationEnabled() {
        return isSuppressWarningsAnnotationEnabled;
    }

    @Nonnull
    @Override
    public Json2JavaSettings setSuppressWarningsAnnotationEnabled(boolean enabled) {
        isSuppressWarningsAnnotationEnabled = enabled;
        return this;
    }
}
