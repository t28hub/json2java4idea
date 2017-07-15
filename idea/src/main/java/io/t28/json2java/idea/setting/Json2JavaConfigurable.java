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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.InputValidatorEx;
import io.t28.json2java.core.JavaConverter;
import io.t28.json2java.idea.Json2JavaBundle;
import io.t28.json2java.idea.inject.GuiceManager;
import io.t28.json2java.idea.inject.JavaConverterFactory;
import io.t28.json2java.idea.view.SettingsPanel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.tuple.Tuple;

import javax.annotation.Nonnull;
import javax.swing.JComponent;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class Json2JavaConfigurable implements SearchableConfigurable {
    private static final String SAMPLE_PACKAGE = "sample";
    private static final String SAMPLE_CLASS = "Sample";
    private static final String SAMPLE_JSON = "{\"id\":1,\"user\":{\"name\":null}}";

    @Inject
    @SuppressWarnings("unused")
    private Json2JavaSettings settings;

    @Inject
    @SuppressWarnings("unused")
    private Json2JavaBundle bundle;

    @Inject
    @Named("ClassPrefix")
    @SuppressWarnings("unused")
    private Provider<InputValidator> prefixValidatorProvider;

    @Inject
    @Named("ClassSuffix")
    @SuppressWarnings("unused")
    private Provider<InputValidator> suffixValidatorProvider;

    @Inject
    @SuppressWarnings("unused")
    private Provider<JavaConverterFactory> javaConverterFactoryProvider;

    @Nullable
    private SettingsPanel panel;

    @SuppressWarnings("unused")
    public Json2JavaConfigurable(@Nonnull Project project) {
        this(project, GuiceManager.getInstance(project));
    }

    // Provides this constructor which has 2 parameters due to avoid crash when instantiate from IntelliJ.
    @VisibleForTesting
    Json2JavaConfigurable(@SuppressWarnings("unused") @Nonnull Project project, @Nonnull GuiceManager guiceManager) {
        final Injector injector = guiceManager.getInjector();
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

        if (panel.isGeneratedAnnotationEnabled() != settings.isGeneratedAnnotationEnabled()) {
            return true;
        }

        return panel.isSuppressWarningsAnnotationEnabled() != settings.isSuppressWarningsAnnotationEnabled();
    }

    @Override
    public void apply() throws ConfigurationException {
        if (panel == null) {
            return;
        }

        final Optional<String> errorMessage = Stream
                .of(
                        Tuple.tuple(panel.getClassNamePrefix(), prefixValidatorProvider.get()),
                        Tuple.tuple(panel.getClassNameSuffix(), suffixValidatorProvider.get())
                )
                .filter(tuple -> {
                    final String text = tuple.v1();
                    final InputValidator validator = tuple.v2();
                    return validator.checkInput(text) && !validator.canClose(text);
                })
                .map(tuple -> {
                    final String text = tuple.v1();
                    final InputValidator validator = tuple.v2();

                    String message = null;
                    if (validator instanceof InputValidatorEx) {
                        message = ((InputValidatorEx) validator).getErrorText(text);
                    }
                    if (Strings.isNullOrEmpty(message)) {
                        message = bundle.message("error.message.unknown");
                    }
                    return message;
                })
                .findFirst();

        if (errorMessage.isPresent()) {
            throw new ConfigurationException(errorMessage.get());
        }

        settings.setStyle(panel.getStyle());
        settings.setClassNamePrefix(panel.getClassNamePrefix());
        settings.setClassNameSuffix(panel.getClassNameSuffix());
        settings.setGeneratedAnnotationEnabled(panel.isGeneratedAnnotationEnabled());
        settings.setSuppressWarningsAnnotationEnabled(panel.isSuppressWarningsAnnotationEnabled());
    }

    @Override
    public void reset() {
        if (panel == null) {
            return;
        }

        panel.setStyle(settings.getStyle());
        panel.setClassNamePrefix(settings.getClassNamePrefix());
        panel.setClassNameSuffix(settings.getClassNameSuffix());
        panel.setGeneratedAnnotationEnabled(settings.isGeneratedAnnotationEnabled());
        panel.setSuppressWarningsAnnotationEnabled(settings.isSuppressWarningsAnnotationEnabled());
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

    @Nonnull
    @Override
    public Runnable enableSearch(String option) {
        // To avoid a crash for AndroidStudio 2.3.x
        return () -> {
        };
    }

    private void updatePreview() {
        if (panel == null) {
            return;
        }

        try {
            final Json2JavaSettings settings = Json2JavaSettings.getInstance()
                    .setStyle(panel.getStyle())
                    .setClassNamePrefix(panel.getClassNamePrefix())
                    .setClassNameSuffix(panel.getClassNameSuffix())
                    .setGeneratedAnnotationEnabled(panel.isGeneratedAnnotationEnabled())
                    .setSuppressWarningsAnnotationEnabled(panel.isSuppressWarningsAnnotationEnabled());
            final JavaConverterFactory converterFactory = javaConverterFactoryProvider.get();
            final JavaConverter converter = converterFactory.create(settings);
            final String java = converter.convert(SAMPLE_PACKAGE, SAMPLE_CLASS, SAMPLE_JSON);
            panel.setPreviewText(java);
        } catch (IOException ignore) {
        }
    }
}
