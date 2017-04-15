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

package io.t28.json2java.idea;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.intellij.ide.IdeView;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.RunResult;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.PlatformIcons;
import io.t28.json2java.idea.command.CommandActionFactory;
import io.t28.json2java.idea.command.NewClassCommandAction;
import io.t28.json2java.idea.exception.ClassAlreadyExistsException;
import io.t28.json2java.idea.exception.InvalidDirectoryException;
import io.t28.json2java.idea.inject.GuiceManager;
import io.t28.json2java.idea.inject.JavaConverterFactory;
import io.t28.json2java.idea.setting.Json2JavaSettings;
import io.t28.json2java.idea.util.Formatter;
import io.t28.json2java.idea.view.NewClassDialog;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

public class NewClassAction extends AnAction implements NewClassDialog.ActionListener {
    private static final Logger LOGGER = Logger.getInstance(NewClassAction.class);
    private static final String NOTIFICATION_DISPLAY_ID = "Json2Java4Idea";

    private Project project;

    private IdeView ideView;

    @Inject
    @SuppressWarnings("unused")
    private Json2JavaBundle bundle;

    @Inject
    @SuppressWarnings("unused")
    private Json2JavaSettings settings;

    @Inject
    @Named("Name")
    @SuppressWarnings("unused")
    private Provider<InputValidator> nameValidatorProvider;

    @Inject
    @Named("Json")
    @SuppressWarnings("unused")
    private Provider<InputValidator> jsonValidatorProvider;

    @Inject
    @SuppressWarnings("unused")
    private Provider<CommandActionFactory> commandActionFactoryProvider;

    @Inject
    @SuppressWarnings("unused")
    private Provider<JavaConverterFactory> javaConverterFactoryProvider;

    @Inject
    @Named("Json")
    @SuppressWarnings("unused")
    private Provider<Formatter> jsonFormatterProvider;

    public NewClassAction() {
        super(PlatformIcons.CLASS_ICON);
    }

    @Override
    public void actionPerformed(@Nonnull AnActionEvent event) {
        if (!isAvailable(event)) {
            return;
        }

        project = event.getProject();
        ideView = event.getData(DataKeys.IDE_VIEW);

        final Injector injector = GuiceManager.getInstance(project).getInjector();
        injector.injectMembers(this);

        // 'selected' is null when directory selection is canceled although multiple directories are chosen.
        final PsiDirectory selected = ideView.getOrChooseDirectory();
        if (selected == null) {
            return;
        }

        final NewClassDialog dialog = NewClassDialog.builder(project, bundle)
                .nameValidator(nameValidatorProvider.get())
                .jsonValidator(jsonValidatorProvider.get())
                .actionListener(this)
                .build();
        dialog.show();
    }

    @Override
    public void update(@Nonnull AnActionEvent event) {
        final boolean isAvailable = isAvailable(event);
        final Presentation presentation = event.getPresentation();
        presentation.setEnabledAndVisible(isAvailable);
    }

    @Override
    public void onOk(@Nonnull NewClassDialog dialog) {
        final PsiDirectory directory = ideView.getOrChooseDirectory();
        if (directory == null) {
            dialog.cancel();
            return;
        }

        try {
            final CommandActionFactory actionFactory = commandActionFactoryProvider.get();
            final JavaConverterFactory converterFactory = javaConverterFactoryProvider.get();
            final NewClassCommandAction command = actionFactory.create(
                    dialog.getClassName(),
                    dialog.getJson(),
                    directory,
                    converterFactory.create(settings)
            );

            final ProgressManager progressManager = ProgressManager.getInstance();
            progressManager.runProcessWithProgressSynchronously(() -> {
                final ProgressIndicator indicator = progressManager.getProgressIndicator();
                if (indicator != null) {
                    indicator.setIndeterminate(true);
                    indicator.setText(bundle.message("progress.text", dialog.getClassName()));
                }

                final RunResult<PsiFile> result = command.execute();
                return result.getResultObject();
            }, bundle.message("progress.title"), false, project);
            dialog.close();
        } catch (RuntimeException e) {
            LOGGER.warn("Unable to create a class", e);
            onError(dialog, e.getCause());
        }
    }

    @Override
    public void onCancel(@Nonnull NewClassDialog dialog) {
        dialog.cancel();
    }

    @Override
    public void onFormat(@Nonnull NewClassDialog dialog) {
        final Formatter formatter = jsonFormatterProvider.get();
        final String formatted = formatter.format(dialog.getJson());
        dialog.setJson(formatted);
    }

    @Override
    public void onSettings(@Nonnull NewClassDialog dialog) {
        ShowSettingsUtil.getInstance().showSettingsDialog(project, bundle.message("settings.name"));
    }

    private void onError(@Nonnull NewClassDialog dialog, @Nullable Throwable cause) {
        if (cause instanceof ClassAlreadyExistsException) {
            // Dialog is not closed or cancelled since user can rename class after message showing
            Messages.showMessageDialog(
                    project,
                    bundle.message("error.message.class.exists", dialog.getClassName()),
                    bundle.message("error.title.cannot.create.class"),
                    Messages.getErrorIcon()
            );
            return;
        }

        if (cause instanceof InvalidDirectoryException) {
            final Notification notification = new Notification(
                    NOTIFICATION_DISPLAY_ID,
                    bundle.message("error.title.directory.invalid"),
                    bundle.message("error.message.directory.invalid"),
                    NotificationType.WARNING
            );
            Notifications.Bus.notify(notification);
            dialog.close();
            return;
        }

        final Notification notification = new Notification(
                NOTIFICATION_DISPLAY_ID,
                bundle.message("error.title.cannot.create.class"),
                bundle.message("error.message.cannot.create", dialog.getClassName()),
                NotificationType.ERROR
        );
        Notifications.Bus.notify(notification);
        dialog.close();
    }

    @CheckReturnValue
    @VisibleForTesting
    @SuppressWarnings("WeakerAccess")
    static boolean isAvailable(@Nonnull AnActionEvent event) {
        final Project project = event.getProject();
        if (project == null) {
            return false;
        }

        final IdeView view = event.getData(LangDataKeys.IDE_VIEW);
        if (view == null) {
            return false;
        }

        final ProjectRootManager rootManager = ProjectRootManager.getInstance(project);
        final ProjectFileIndex fileIndex = rootManager.getFileIndex();
        final Optional<PsiDirectory> sourceDirectory = Stream.of(view.getDirectories())
                .filter(directory -> {
                    final VirtualFile virtualFile = directory.getVirtualFile();
                    return fileIndex.isUnderSourceRootOfType(virtualFile, JavaModuleSourceRootTypes.SOURCES);
                })
                .findFirst();
        return sourceDirectory.isPresent();
    }
}
