package io.t28.pojojson.idea.view;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.uiDesigner.core.GridConstraints;
import io.t28.pojojson.idea.PluginBundle;
import io.t28.pojojson.idea.validator.NullValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.tuple.Tuple;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

public class NewClassDialog extends DialogWrapper {
    private static final String EMPTY_TEXT = "";

    private final Project project;

    private final InputValidator nameValidator;
    private final InputValidator jsonValidator;
    private final ActionListener actionListener;

    private final Action formatAction;

    private JPanel centerPanel;
    private JTextField nameTextField;
    private JComponent jsonEditorComponent;
    private Editor jsonEditor;
    private Document jsonDocument;

    private NewClassDialog(@NotNull Builder builder) {
        super(builder.project, true);
        this.project = builder.project;
        this.nameValidator = builder.nameValidator;
        this.jsonValidator = builder.jsonValidator;
        this.actionListener = builder.actionListener;

        final PluginBundle bundle = builder.bundle;
        this.formatAction = new FormatAction(bundle.message("dialog.action.format"));

        setTitle(bundle.message("dialog.title"));
        setResizable(true);
        init();
    }

    @NotNull
    public static Builder builder(@NotNull Project project, @Nonnull PluginBundle bundle) {
        return new Builder(project, bundle);
    }

    public void close() {
        close(OK_EXIT_CODE);
    }

    public void cancel() {
        close(CANCEL_EXIT_CODE);
    }

    @NotNull
    @CheckReturnValue
    public String getClassName() {
        final String text = nameTextField.getText();
        return Strings.nullToEmpty(text).trim();
    }

    @NotNull
    @CheckReturnValue
    public String getJson() {
        return jsonDocument.getText().trim();
    }

    @NotNull
    @CheckReturnValue
    public Editor getJsonEditor() {
        return jsonEditor;
    }

    @NotNull
    @CheckReturnValue
    public Document getJsonDocument() {
        return jsonDocument;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return nameTextField;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        final EditorFactory editorFactory = EditorFactory.getInstance();
        jsonDocument = editorFactory.createDocument(EMPTY_TEXT);
        jsonEditor = editorFactory.createEditor(jsonDocument, project, JsonFileType.INSTANCE, false);
        final EditorSettings settings = jsonEditor.getSettings();
        settings.setLineNumbersShown(true);
        settings.setAdditionalColumnsCount(0);
        settings.setAdditionalLinesCount(0);
        settings.setRightMarginShown(false);
        settings.setFoldingOutlineShown(false);
        settings.setLineMarkerAreaShown(false);
        settings.setIndentGuidesShown(false);
        settings.setVirtualSpace(false);
        settings.setWheelFontChangeEnabled(false);

        final EditorColorsScheme colorsScheme = jsonEditor.getColorsScheme();
        colorsScheme.setColor(EditorColors.CARET_ROW_COLOR, null);

        final GridConstraints constraints = new GridConstraints(
                1, 1, 1, 1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                3,
                3,
                new Dimension(480, 300),
                null,
                null
        );
        jsonEditorComponent = jsonEditor.getComponent();
        jsonEditor.getContentComponent().setFocusable(true);
        ((JPanel) centerPanel.getComponent(0)).add(jsonEditorComponent, constraints);
        return centerPanel;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return ImmutableList
                .of(
                        Tuple.tuple(getClassName(), nameValidator, nameTextField),
                        Tuple.tuple(getJson(), jsonValidator, jsonEditorComponent)
                )
                .stream()
                .filter(tuple -> {
                    final String text = tuple.v1();
                    final InputValidator validator = tuple.v2();
                    return validator.checkInput(text);
                })
                .filter(tuple -> {
                    final String text = tuple.v1();
                    final InputValidator validator = tuple.v2();
                    return !validator.canClose(text);
                })
                .map(tuple -> {
                    final String text = tuple.v1();
                    final InputValidator validator = tuple.v2();

                    String message = null;
                    if (validator instanceof InputValidatorEx) {
                        message = ((InputValidatorEx) validator).getErrorText(text);
                    }
                    if (Strings.isNullOrEmpty(message)) {
                        message = "Unknown error occurs";
                    }
                    return new ValidationInfo(message, tuple.v3());
                })
                .findFirst()
                .orElseGet(() -> super.doValidate());
    }

    @Override
    protected void doOKAction() {
        if (myOKAction.isEnabled()) {
            actionListener.onOk(this);
        }
    }

    @Override
    public void doCancelAction() {
        if (myCancelAction.isEnabled()) {
            actionListener.onCancel(this);
        }
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction()};
    }

    @NotNull
    @Override
    protected Action[] createLeftSideActions() {
        return new Action[]{formatAction};
    }

    class FormatAction extends DialogWrapperAction {
        FormatAction(@Nonnull String name) {
            super(name);
        }

        @Override
        protected void doAction(@Nonnull ActionEvent event) {
            if (isEnabled()) {
                actionListener.onFormat(NewClassDialog.this);
            }
        }
    }

    public interface ActionListener {
        default void onOk(@Nonnull NewClassDialog dialog) {
        }

        default void onCancel(@Nonnull NewClassDialog dialog) {
        }

        default void onFormat(@Nonnull NewClassDialog dialog) {
        }
    }

    public static class Builder {
        private final Project project;
        private final PluginBundle bundle;
        private InputValidator nameValidator;
        private InputValidator jsonValidator;
        private ActionListener actionListener;

        private Builder(@NotNull Project project, @Nonnull PluginBundle bundle) {
            this.project = project;
            this.bundle = bundle;
            this.nameValidator = new NullValidator();
            this.jsonValidator = new NullValidator();
            this.actionListener = new ActionListener() {
            };
        }

        @NotNull
        @CheckReturnValue
        public Builder nameValidator(@NotNull InputValidator nameValidator) {
            this.nameValidator = nameValidator;
            return this;
        }

        @NotNull
        @CheckReturnValue
        public Builder jsonValidator(@NotNull InputValidator jsonValidator) {
            this.jsonValidator = jsonValidator;
            return this;
        }

        @NotNull
        @CheckReturnValue
        public Builder actionListener(@NotNull ActionListener actionListener) {
            this.actionListener = actionListener;
            return this;
        }

        @NotNull
        @CheckReturnValue
        public NewClassDialog build() {
            return new NewClassDialog(this);
        }
    }
}
