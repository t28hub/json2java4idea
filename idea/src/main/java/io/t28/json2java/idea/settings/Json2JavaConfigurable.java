package io.t28.json2java.idea.settings;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.inject.Injector;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import io.t28.json2java.core.JavaConverter;
import io.t28.json2java.idea.Json2JavaBundle;
import io.t28.json2java.idea.inject.GuiceManager;
import io.t28.json2java.idea.inject.JavaConverterFactory;
import io.t28.json2java.idea.view.SettingsPanel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.swing.JComponent;
import java.io.IOException;

public class Json2JavaConfigurable implements SearchableConfigurable {
    private static final String SAMPLE_PACKAGE = "sample";
    private static final String SAMPLE_CLASS = "Sample";
    private static final String SAMPLE_JSON = "{\"id\":1}";

    private final Injector injector;

    private final Json2JavaSettings settings;

    private final Json2JavaBundle bundle;

    @Nullable
    private SettingsPanel panel;

    @SuppressWarnings("unused")
    public Json2JavaConfigurable(@Nonnull Project project) {
        this(
                GuiceManager.getInstance(project).getInjector(),
                Json2JavaSettings.getInstance(project),
                Json2JavaBundle.getInstance()
        );
    }

    @VisibleForTesting
    Json2JavaConfigurable(@Nonnull Injector injector, @Nonnull Json2JavaSettings settings, Json2JavaBundle bundle) {
        this.injector = injector;
        this.settings = settings;
        this.bundle = bundle;
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
            panel = new SettingsPanel() {
                @Override
                protected void onChanged() {
                    updatePreview();
                }
            };
            updatePreview();
        }

        reset();
        return panel.getComponent();
    }

    @Override
    public boolean isModified() {
        if (panel == null) {
            return true;
        }

        if (!Objects.equal(panel.getStyle(), settings.getStyle())) {
            return true;
        }

        if (!Objects.equal(panel.getClassNamePrefix(), settings.getClassNamePrefix())) {
            return true;
        }

        if (!Objects.equal(panel.getClassNameSuffix(), settings.getClassNameSuffix())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        if (panel == null) {
            return;
        }

        settings.setStyle(panel.getStyle());
        settings.setClassNamePrefix(panel.getClassNamePrefix());
        settings.setClassNameSuffix(panel.getClassNameSuffix());
    }

    @Override
    public void reset() {
        if (panel == null) {
            return;
        }

        panel.setStyle(settings.getStyle());
        panel.setClassNamePrefix(settings.getClassNamePrefix());
        panel.setClassNameSuffix(settings.getClassNameSuffix());
        updatePreview();
    }

    @Override
    public void disposeUIResources() {
        if (panel == null) {
            return;
        }

        panel.dispose();
        panel = null;
    }

    private void updatePreview() {
        if (panel == null) {
            return;
        }

        final Json2JavaSettings settings = Json2JavaSettings.getInstance()
                .setStyle(panel.getStyle())
                .setClassNamePrefix(panel.getClassNamePrefix())
                .setClassNameSuffix(panel.getClassNameSuffix());
        final JavaConverterFactory converterFactory = injector.getInstance(JavaConverterFactory.class);
        final JavaConverter converter = converterFactory.create(settings);

        try {
            final String java = converter.convert(SAMPLE_PACKAGE, SAMPLE_CLASS, SAMPLE_JSON);
            panel.setPreviewText(java);
        } catch (IOException ignore) {
        }
    }
}
