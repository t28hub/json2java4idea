package io.t28.json2java.idea;

import com.google.common.annotations.VisibleForTesting;
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
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.util.PlatformIcons;
import io.t28.json2java.idea.command.CommandActionFactory;
import io.t28.json2java.idea.command.NewClassCommandAction;
import io.t28.json2java.idea.exceptions.ClassAlreadyExistsException;
import io.t28.json2java.idea.exceptions.InvalidDirectoryException;
import io.t28.json2java.idea.inject.GuiceManager;
import io.t28.json2java.idea.inject.JavaConverterFactory;
import io.t28.json2java.idea.settings.Json2JavaSettings;
import io.t28.json2java.idea.utils.Formatter;
import io.t28.json2java.idea.utils.JsonFormatter;
import io.t28.json2java.idea.view.NewClassDialog;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

public class NewClassAction extends AnAction implements NewClassDialog.ActionListener {
    private static final String NOTIFICATION_DISPLAY_ID = "Json2Java4Idea";

    private Project project;

    private IdeView ideView;

    private Injector injector;

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
        injector = GuiceManager.getInstance(project).getInjector();
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

        final CommandActionFactory actionFactory = injector.getInstance(CommandActionFactory.class);
        final JavaConverterFactory converterFactory = injector.getInstance(JavaConverterFactory.class);
        final NewClassCommandAction action = actionFactory.create(
                dialog.getClassName(),
                dialog.getJson(),
                directory,
                converterFactory.create(settings)
        );

        try {
            action.execute().getResultObject();
            dialog.close();
        } catch (RuntimeException e) {
            onError(dialog, e.getCause());
        }
    }

    @Override
    public void onCancel(@Nonnull NewClassDialog dialog) {
        dialog.cancel();
    }

    @Override
    public void onFormat(@Nonnull NewClassDialog dialog) {
        final Formatter formatter = injector.getInstance(JsonFormatter.class);
        final String formatted = formatter.format(dialog.getJson());
        dialog.setJson(formatted);
    }

    @Override
    public void onSettings(@Nonnull NewClassDialog dialog) {
        ShowSettingsUtil.getInstance().showSettingsDialog(project, "Json2Java");
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
