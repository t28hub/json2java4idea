package io.t28.json2java.idea.settings;

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

    void setStyle(@Nonnull Style style);

    @Nonnull
    @CheckReturnValue
    String getClassNamePrefix();

    void setClassNamePrefix(@Nonnull String classNamePrefix);

    @Nonnull
    @CheckReturnValue
    String getClassNameSuffix();

    void setClassNameSuffix(@Nonnull String classNameSuffix);
}
