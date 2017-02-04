package io.t28.pojojson.idea;

import com.google.common.base.Strings;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class NewClassDialog extends DialogWrapper {
    private static final String EMPTY_TEXT = "";

    private final Project project;
    private final EditorFactory editorFactory;

    private JPanel centerPanel;
    private JTextField nameTextField;
    private JComboBox typeComboBox;
    private JPanel jsonEditorPanel;

    private Editor jsonEditor;
    private Document jsonDocument;

    public NewClassDialog(@NotNull Project project) {
        super(project, true);
        this.project = project;
        this.editorFactory = EditorFactory.getInstance();
        setTitle("Create New Class from JSON");
        setResizable(true);
        init();
    }

    @NotNull
    public String getName() {
        final String name = nameTextField.getText();
        return Strings.nullToEmpty(name);
    }

    @NotNull
    public String getType() {
        final Object selected = typeComboBox.getSelectedItem();
        return Strings.nullToEmpty((String) selected);
    }

    @NotNull
    public String getJson() {
        return jsonDocument.getText();
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
    protected JComponent createCenterPanel() {
        jsonDocument = editorFactory.createDocument("");
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
}
