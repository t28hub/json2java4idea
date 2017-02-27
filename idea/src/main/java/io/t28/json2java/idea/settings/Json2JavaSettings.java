package io.t28.json2java.idea.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import io.t28.json2java.core.Style;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

@State(
        name = "Json2JavaSettings",
        storages = @Storage(value = "$PROJECT_CONFIG_DIR$/json2java.xml")
)
public class Json2JavaSettings implements PersistentStateComponent<Element> {
    private static final String NO_TEXT = "";
    private static final String ROOT_ELEMENT = "component";
    private static final String CLASS_STYLE_ATTRIBUTE = "style";
    private static final String CLASS_NAME_PREFIX_ATTRIBUTE = "classNamePrefix";
    private static final String CLASS_NAME_SUFFIX_ATTRIBUTE = "classNameSuffix";

    private Style style;
    private String classNamePrefix;
    private String classNameSuffix;

    public Json2JavaSettings() {
        style = Style.NONE;
        classNamePrefix = NO_TEXT;
        classNameSuffix = NO_TEXT;
    }

    @Nonnull
    @CheckReturnValue
    public static Json2JavaSettings getInstance(@Nonnull Project project) {
        return ServiceManager.getService(project, Json2JavaSettings.class);
    }

    @Nullable
    @Override
    public Element getState() {
        final Element state = new Element(ROOT_ELEMENT);
        state.setAttribute(CLASS_STYLE_ATTRIBUTE, style.name());
        state.setAttribute(CLASS_NAME_PREFIX_ATTRIBUTE, classNamePrefix);
        state.setAttribute(CLASS_NAME_SUFFIX_ATTRIBUTE, classNameSuffix);
        return state;
    }

    @Override
    public void loadState(@Nonnull Element state) {
        style = Style.fromName(state.getAttributeValue(CLASS_STYLE_ATTRIBUTE), Style.NONE);
        classNamePrefix = state.getAttributeValue(CLASS_NAME_PREFIX_ATTRIBUTE, NO_TEXT);
        classNameSuffix = state.getAttributeValue(CLASS_NAME_SUFFIX_ATTRIBUTE, NO_TEXT);
    }

    @Override
    public void noStateLoaded() {
        style = Style.NONE;
        classNamePrefix = NO_TEXT;
        classNameSuffix = NO_TEXT;
    }

    @Nonnull
    @CheckReturnValue
    public Style getStyle() {
        return style;
    }

    void setStyle(@Nonnull Style classStyle) {
        this.style = classStyle;
    }

    @Nonnull
    @CheckReturnValue
    public String getClassNamePrefix() {
        return classNamePrefix;
    }

    void setClassNamePrefix(@Nonnull String classNamePrefix) {
        this.classNamePrefix = classNamePrefix;
    }

    @Nonnull
    @CheckReturnValue
    public String getClassNameSuffix() {
        return classNameSuffix;
    }

    void setClassNameSuffix(@Nonnull String classNameSuffix) {
        this.classNameSuffix = classNameSuffix;
    }
}
