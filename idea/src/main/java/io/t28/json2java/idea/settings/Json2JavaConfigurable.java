package io.t28.json2java.idea.settings;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import io.t28.json2java.core.Configuration;
import io.t28.json2java.core.JavaConverter;
import io.t28.json2java.idea.Json2JavaBundle;
import io.t28.json2java.idea.naming.ClassNamePolicy;
import io.t28.json2java.idea.naming.FieldNamePolicy;
import io.t28.json2java.idea.naming.MethodNamePolicy;
import io.t28.json2java.idea.naming.ParameterNamePolicy;
import io.t28.json2java.idea.utils.PsiTypeConverter;
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
    private final Json2JavaSettings settings;
    private final Json2JavaBundle bundle;

    @Nullable
    private SettingsPanel panel;

    @SuppressWarnings("unused")
    public Json2JavaConfigurable(@Nonnull Project project) {
        this(project, Json2JavaSettings.getInstance(project), Json2JavaBundle.getInstance());
    }

    @VisibleForTesting
    Json2JavaConfigurable(@Nonnull Project project, @Nonnull Json2JavaSettings settings, @Nonnull Json2JavaBundle bundle) {
        this.project = project;
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
                try {
                    final Configuration configuration = Configuration.builder()
                            .style(panel.getStyle())
                            .classNamePolicy(new ClassNamePolicy(PsiNameHelper.getInstance(project), panel.getClassNamePrefix(), panel.getClassNameSuffix()))
                            .fieldNamePolicy(new FieldNamePolicy(PsiNameHelper.getInstance(project), JavaCodeStyleManager.getInstance(project)))
                            .methodNamePolicy(new MethodNamePolicy(project, PsiNameHelper.getInstance(project), new PsiTypeConverter(PsiManager.getInstance(project))))
                            .parameterNamePolicy(new ParameterNamePolicy(PsiNameHelper.getInstance(project), JavaCodeStyleManager.getInstance(project)))
                            .build();
                    final JavaConverter converter = new JavaConverter(configuration);
                    final String java = converter.convert(SAMPLE_PACKAGE, SAMPLE_CLASS, SAMPLE_JSON);
                    panel.setPreviewText(java);
                } catch (IOException ignore) {
                }
            });
        }, null, null);
    }
}
