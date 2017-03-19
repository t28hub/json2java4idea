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

package io.t28.json2java.idea.view;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.CaretModel;
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
import io.t28.json2java.idea.Json2JavaBundle;
import io.t28.json2java.idea.validator.NullValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.tuple.Tuple;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;

public class NewClassDialog extends DialogWrapper {
    private static final String EMPTY_TEXT = "";
    private static final int INITIAL_OFFSET = 0;

    private final Project project;
    private final Json2JavaBundle bundle;

    private final InputValidator nameValidator;
    private final InputValidator jsonValidator;
    private final ActionListener actionListener;

    private final Action formatAction;
    private final Action settingsAction;

    private JPanel centerPanel;
    private JTextField nameTextField;
    private JPanel jsonPanel;

    private Editor jsonEditor;
    private Document jsonDocument;

    private NewClassDialog(@NotNull Builder builder) {
        super(builder.project, true);
        this.project = builder.project;
        this.bundle = builder.bundle;
        this.nameValidator = builder.nameValidator;
        this.jsonValidator = builder.jsonValidator;
        this.actionListener = builder.actionListener;

        this.formatAction = new FormatAction(bundle.message("dialog.action.format"));
        this.settingsAction = new SettingsAction(bundle.message("dialog.action.settings"));

        setTitle(bundle.message("dialog.title"));
        setResizable(true);
        init();
    }

    @NotNull
    public static Builder builder(@NotNull Project project, @Nonnull Json2JavaBundle bundle) {
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

    public void setJson(@NotNull String json) {
        final CommandProcessor processor = CommandProcessor.getInstance();
        processor.executeCommand(project, () -> {
            final Application application = ApplicationManager.getApplication();
            application.runWriteAction(() -> {
                jsonDocument.replaceString(INITIAL_OFFSET, jsonDocument.getTextLength(), json);

                final int textLength = jsonDocument.getTextLength();
                final CaretModel caret = jsonEditor.getCaretModel();
                if (caret.getOffset() >= textLength) {
                    caret.moveToOffset(textLength);
                }
            });
        }, null, null);
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return nameTextField;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return centerPanel;
    }

    @Override
    protected void dispose() {
        if (jsonEditor != null && !jsonEditor.isDisposed()) {
            EditorFactory.getInstance().releaseEditor(jsonEditor);
        }
        super.dispose();
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return ImmutableList
                .of(
                        Tuple.tuple(getClassName(), nameValidator, nameTextField),
                        Tuple.tuple(getJson(), jsonValidator, jsonPanel)
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
                        message = bundle.message("error.message.unknown");
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
        return new Action[]{settingsAction, formatAction};
    }

    private void createUIComponents() {
        final EditorFactory editorFactory = EditorFactory.getInstance();
        jsonDocument = editorFactory.createDocument(EMPTY_TEXT);
        jsonEditor = editorFactory.createEditor(jsonDocument, project, JsonFileType.INSTANCE, false);

        final EditorSettings settings = jsonEditor.getSettings();
        settings.setWhitespacesShown(true);
        settings.setLineMarkerAreaShown(false);
        settings.setIndentGuidesShown(false);
        settings.setLineNumbersShown(true);
        settings.setFoldingOutlineShown(false);
        settings.setRightMarginShown(false);
        settings.setVirtualSpace(false);
        settings.setWheelFontChangeEnabled(false);
        settings.setUseSoftWraps(false);
        settings.setAdditionalColumnsCount(0);
        settings.setAdditionalLinesCount(1);

        final EditorColorsScheme colorsScheme = jsonEditor.getColorsScheme();
        colorsScheme.setColor(EditorColors.CARET_ROW_COLOR, null);

        jsonEditor.getContentComponent().setFocusable(true);
        jsonPanel = (JPanel) jsonEditor.getComponent();
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

    class SettingsAction extends DialogWrapperAction {
        SettingsAction(@Nonnull String name) {
            super(name);
        }

        @Override
        protected void doAction(ActionEvent e) {
            if (isEnabled()) {
                actionListener.onSettings(NewClassDialog.this);
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

        default void onSettings(@Nonnull NewClassDialog dialog) {
        }
    }

    public static class Builder {
        private final Project project;
        private final Json2JavaBundle bundle;
        private InputValidator nameValidator;
        private InputValidator jsonValidator;
        private ActionListener actionListener;

        private Builder(@NotNull Project project, @Nonnull Json2JavaBundle bundle) {
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
