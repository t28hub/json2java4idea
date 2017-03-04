package io.t28.json2java.idea.settings;

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
