package io.t28.json2java.idea.settings;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import io.t28.json2java.core.Style;
import io.t28.json2java.idea.view.SettingsPanel;
import io.t28.json2java.idea.Json2JavaBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

public class Json2JavaConfigurable implements SearchableConfigurable {
    private final Json2JavaBundle bundle;
    private final Json2JavaSettings settings;

    @Nullable
    private SettingsPanel panel;

    @SuppressWarnings("unused")
    public Json2JavaConfigurable(@Nonnull Project project) {
        this(Json2JavaBundle.getInstance(), Json2JavaSettings.getInstance(project));
    }

    @VisibleForTesting
    Json2JavaConfigurable(@Nonnull Json2JavaBundle bundle, @Nonnull Json2JavaSettings settings) {
        this.bundle = bundle;
        this.settings = settings;
    }

    @NotNull
    @Override
    public String getId() {
        return getDisplayName();
    }

    @Nls
    @Override
    public String getDisplayName() {
        return bundle.message("settings.name");
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return getDisplayName();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (panel == null) {
            panel = new SettingsPanel();
        }

        reset();
        return panel.getComponent();
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean isModified() {
        if (panel == null) {
            return true;
        }

        return !Objects.equal(Style.fromName(panel.getClassStyle(), Style.NONE), settings.getStyle())
                || !Objects.equal(panel.getClassNamePrefix(), settings.getClassNamePrefix())
                || !Objects.equal(panel.getClassNameSuffix(), settings.getClassNameSuffix());
    }

    @Override
    public void apply() throws ConfigurationException {
        if (panel == null) {
            return;
        }

        settings.setStyle(Style.fromName(panel.getClassStyle(), Style.NONE));
        settings.setClassNamePrefix(panel.getClassNamePrefix());
        settings.setClassNameSuffix(panel.getClassNameSuffix());
    }

    @Override
    public void reset() {
        if (panel == null) {
            return;
        }

        panel.setClassStyle(settings.getStyle().name());
        panel.setClassNamePrefix(settings.getClassNamePrefix());
        panel.setClassNameSuffix(settings.getClassNameSuffix());
    }

    @Override
    public void disposeUIResources() {
        if (panel == null) {
            return;
        }

        panel.dispose();
        panel = null;
    }
}
