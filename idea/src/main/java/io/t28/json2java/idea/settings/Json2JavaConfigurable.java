package io.t28.json2java.idea.settings;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
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
    private static final String SAMPLE_JSON = "{\"id\":1,\"name\":\"foo\",\"is_public\":true,\"tags\":[\"bar\"],\"author\":null}";

    private final Project project;
    private final Injector injector;

    @Inject
    private Json2JavaSettings settings;

    @Inject
    private Json2JavaBundle bundle;

    @Nullable
    private SettingsPanel panel;

    @SuppressWarnings("unused")
    public Json2JavaConfigurable(@Nonnull Project project) {
        this(project, GuiceManager.getInstance(project));
    }

    @VisibleForTesting
    Json2JavaConfigurable(@Nonnull Project project, @Nonnull GuiceManager guiceManager) {
        this(project, guiceManager.getInjector());
    }

    @VisibleForTesting
    Json2JavaConfigurable(@Nonnull Project project, @Nonnull Injector injector) {
        this.project = project;
        this.injector = injector;
        injector.injectMembers(this);
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

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean isModified() {
        if (panel == null) {
            return true;
        }

        return !Objects.equal(panel.getStyle(), settings.getStyle())
                || !Objects.equal(panel.getClassNamePrefix(), settings.getClassNamePrefix())
                || !Objects.equal(panel.getClassNameSuffix(), settings.getClassNameSuffix());
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

        CommandProcessor.getInstance().executeCommand(project, () -> {
            final Application application = ApplicationManager.getApplication();
            application.runWriteAction(() -> {
                final Json2JavaSettings settings = Json2JavaSettings.getInstance();
                settings.setStyle(panel.getStyle());
                settings.setClassNamePrefix(panel.getClassNamePrefix());
                settings.setClassNameSuffix(panel.getClassNameSuffix());
                try {
                    final JavaConverterFactory converterFactory = injector.getInstance(JavaConverterFactory.class);
                    final JavaConverter converter = converterFactory.create(settings);
                    final String java = converter.convert(SAMPLE_PACKAGE, SAMPLE_CLASS, SAMPLE_JSON);
                    panel.setPreviewText(java);
                } catch (IOException ignore) {
                }
            });
        }, null, null);
    }
}
