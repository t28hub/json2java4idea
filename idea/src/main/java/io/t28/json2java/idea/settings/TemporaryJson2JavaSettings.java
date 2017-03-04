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

    @Override
    public void setStyle(@Nonnull Style style) {
        this.style = style;
    }

    @Nonnull
    @Override
    public String getClassNamePrefix() {
        return classNamePrefix;
    }

    @Override
    public void setClassNamePrefix(@Nonnull String classNamePrefix) {
        this.classNamePrefix = classNamePrefix;
    }

    @Nonnull
    @Override
    public String getClassNameSuffix() {
        return classNameSuffix;
    }

    @Override
    public void setClassNameSuffix(@Nonnull String classNameSuffix) {
        this.classNameSuffix = classNameSuffix;
    }
}
