package io.t28.pojojson.idea;

import com.intellij.ide.IdeView;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.PlatformIcons;
import io.t28.pojojson.core.ClassStyle;
import io.t28.pojojson.core.Context;
import io.t28.pojojson.core.PojoJson;
import io.t28.pojojson.idea.ui.NewClassDialog;
import io.t28.pojojson.idea.utils.GsonFormatter;
import io.t28.pojojson.idea.utils.JsonFormatter;
import io.t28.pojojson.idea.validator.JsonValidator;
import io.t28.pojojson.idea.validator.NameValidator;
import io.t28.pojojson.idea.validator.TypeValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;

import java.io.IOException;

public class NewClassAction extends AnAction implements NewClassDialog.ActionListener {
    private Project project;
    private IdeView ideView;
    private JsonFormatter formatter;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        this.project = event.getProject();
        this.formatter = new GsonFormatter();
        if (project == null) {
            throw new IllegalStateException("AnActionEvent.getProject() returns null");
        }

        ideView = event.getData(LangDataKeys.IDE_VIEW);
        if (ideView == null) {
            throw new IllegalStateException("Provided an instance of IdeView is null");
        }

        final PsiDirectory directory = ideView.getOrChooseDirectory();
        if (directory == null) {
            throw new IllegalStateException("Selected directory is null");
        }

        final NewClassDialog dialog = NewClassDialog.builder(project)
                .editorFactory(EditorFactory.getInstance())
                .nameValidator(new NameValidator(project))
                .typeValidator(new TypeValidator())
                .jsonValidator(new JsonValidator())
                .actionListener(this)
                .build();
        dialog.show();
    }

    @Override
    public void update(AnActionEvent event) {
        final IdeView view = event.getData(LangDataKeys.IDE_VIEW);
        if (view == null) {
            event.getPresentation().setEnabledAndVisible(false);
            return;
        }

        final Project project = event.getData(LangDataKeys.PROJECT);
        if (project == null) {
            event.getPresentation().setEnabledAndVisible(false);
            return;
        }

        final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        final PsiDirectory[] directories = view.getDirectories();
        for (final PsiDirectory directory : directories) {
            final boolean isSourceRoot = fileIndex.isUnderSourceRootOfType(directory.getVirtualFile(), JavaModuleSourceRootTypes.SOURCES);
            if (isSourceRoot) {
                event.getPresentation().setIcon(PlatformIcons.CLASS_ICON);
                event.getPresentation().setEnabledAndVisible(true);
                return;
            }
        }
        event.getPresentation().setEnabledAndVisible(false);
    }

    @Override
    public void onOk(@NotNull NewClassDialog dialog) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            final PsiDirectory directory = ideView.getOrChooseDirectory();
            final PsiFileFactory factory = PsiFileFactory.getInstance(project);
            final JavaDirectoryService directoryService = JavaDirectoryService.getInstance();
            final String packageName = directoryService.getPackage(directory).getQualifiedName();

            final ClassStyle style = ClassStyle.fromName(dialog.getType()).orElse(ClassStyle.MODEL);
            final String name = dialog.getName();
            final String json = dialog.getJson();
            final Context context = Context.builder()
                    .style(style)
                    .build();
            final PojoJson pojoJson = new PojoJson(context);
            try {
                final String generated = pojoJson.generate(packageName, name, json);
                final PsiFile file = factory.createFileFromText(name + ".java", JavaFileType.INSTANCE, generated);
                directory.add(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        dialog.close(0);
    }

    @Override
    public void onCancel(@NotNull NewClassDialog dialog) {

    }

    @Override
    public void onFormat(@NotNull NewClassDialog dialog) {
        try {
            final String json = dialog.getJson();
            final String formatted = formatter.format(json);
            dialog.setJson(formatted);
        } catch (IOException e) {
            final Notification notification = new Notification(
                    "POJO.json",
                    "I/O error",
                    "An I/O error occurred",
                    NotificationType.WARNING
            );
            Notifications.Bus.notify(notification, project);
        } catch (IllegalArgumentException e) {
            final Notification notification = new Notification(
                    "POJO.json",
                    "Syntax error",
                    "Specified JSON has syntax error",
                    NotificationType.WARNING
            );
            Notifications.Bus.notify(notification, project);
        }
    }
}
