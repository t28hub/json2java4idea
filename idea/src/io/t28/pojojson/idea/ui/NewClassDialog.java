package io.t28.pojojson.idea.ui;

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
import io.t28.pojojson.idea.Type;
import io.t28.pojojson.idea.utils.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class NewClassDialog extends DialogWrapper {
    private static final String EMPTY_TEXT = "";

    private final Project project;
    private final EditorFactory editorFactory;
    private final InputValidator nameValidator;
    private final InputValidator typeValidator;
    private final InputValidator jsonValidator;

    private JPanel centerPanel;
    private JTextField nameTextField;
    private JComboBox<Type> typeComboBox;
    private JPanel jsonEditorPanel;

    private Editor jsonEditor;
    private Document jsonDocument;

    private NewClassDialog(@NotNull Builder builder) {
        super(builder.project, true);
        this.project = builder.project;
        this.editorFactory = builder.editorFactory;
        this.nameValidator = builder.nameValidator;
        this.typeValidator = builder.typeValidator;
        this.jsonValidator = builder.jsonValidator;

        setTitle("Create New Class from JSON");
        setResizable(true);
        init();
    }

    @NotNull
    public static Builder builder(@NotNull Project project) {
        return new Builder(project);
    }

    @NotNull
    public String getName() {
        final String name = nameTextField.getText();
        return Strings.nullToEmpty(name).trim();
    }

    @NotNull
    public String getType() {
        final Object selected = typeComboBox.getSelectedItem();
        return Strings.nullToEmpty((String) selected).trim();
    }

    @NotNull
    public String getJson() {
        return jsonDocument.getText().trim();
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return nameTextField;
    }

    @Override
    protected void dispose() {
        editorFactory.releaseEditor(jsonEditor);
        super.dispose();
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return ImmutableList
                .of(
                        Tuple.tuple(getName(), nameValidator, nameTextField),
                        Tuple.tuple(getType(), typeValidator, typeComboBox),
                        Tuple.tuple(getJson(), jsonValidator, jsonEditorPanel)
                )
                .stream()
                .filter(tuple -> {
                    final String text = tuple.value1();
                    final InputValidator validator = tuple.value2();
                    return validator.checkInput(text);
                })
                .filter(tuple -> {
                    final String text = tuple.value1();
                    final InputValidator validator = tuple.value2();
                    return !validator.canClose(text);
                })
                .map(tuple -> {
                    final String text = tuple.value1();
                    final InputValidator validator = tuple.value2();

                    String message = null;
                    if (validator instanceof InputValidatorEx) {
                        message = ((InputValidatorEx) validator).getErrorText(text);
                    }
                    return new ValidationInfo(message, tuple.value3());
                })
                .findFirst()
                .orElseGet(() -> super.doValidate());
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
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

        final JComponent editorComponent = jsonEditor.getComponent();
        editorComponent.setMinimumSize(new Dimension(480, 300));
        jsonEditorPanel.add(editorComponent, BorderLayout.CENTER);
        return centerPanel;
    }

    public static class Builder {
        private final Project project;
        private EditorFactory editorFactory;
        private InputValidator nameValidator;
        private InputValidator typeValidator;
        private InputValidator jsonValidator;

        private Builder(@NotNull Project project) {
            this.project = project;
            this.editorFactory = EditorFactory.getInstance();
            this.nameValidator = new NullValidator();
            this.typeValidator = new NullValidator();
            this.jsonValidator = new NullValidator();
        }

        @NotNull
        public Builder editorFactory(@NotNull EditorFactory editorFactory) {
            this.editorFactory = editorFactory;
            return this;
        }

        @NotNull
        public Builder nameValidator(@NotNull InputValidator nameValidator) {
            this.nameValidator = nameValidator;
            return this;
        }

        @NotNull
        public Builder typeValidator(@NotNull InputValidator typeValidator) {
            this.typeValidator = typeValidator;
            return this;
        }

        @NotNull
        public Builder jsonValidator(@NotNull InputValidator jsonValidator) {
            this.jsonValidator = jsonValidator;
            return this;
        }

        @NotNull
        public NewClassDialog build() {
            return new NewClassDialog(this);
        }
    }
}
