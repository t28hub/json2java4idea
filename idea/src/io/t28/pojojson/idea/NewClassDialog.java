package io.t28.pojojson.idea;

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
    private final Project project;
    private final EditorFactory editorFactory;

    private JPanel centerPanel;
    private JTextField nameTextField;
    private JComboBox typeComboBox;
    private JPanel jsonEditorPanel;

    public NewClassDialog(@NotNull Project project) {
        super(project, true);
        this.project = project;
        this.editorFactory = EditorFactory.getInstance();
        setTitle("Create New Class from JSON");
        setResizable(true);
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        final Document document = editorFactory.createDocument("");
        final Editor editor = editorFactory.createEditor(document, project, JsonFileType.INSTANCE, false);
        final EditorSettings settings = editor.getSettings();
        settings.setLineNumbersShown(true);
        settings.setAdditionalColumnsCount(0);
        settings.setAdditionalLinesCount(0);
        settings.setRightMarginShown(false);
        settings.setFoldingOutlineShown(false);
        settings.setLineMarkerAreaShown(false);
        settings.setIndentGuidesShown(false);
        settings.setVirtualSpace(false);
        settings.setWheelFontChangeEnabled(false);

        final EditorColorsScheme colorsScheme = editor.getColorsScheme();
        colorsScheme.setColor(EditorColors.CARET_ROW_COLOR, null);

        final JComponent editorComponent = editor.getComponent();
        editorComponent.setMinimumSize(new Dimension(480, 300));
        jsonEditorPanel.add(editorComponent, BorderLayout.CENTER);
        return centerPanel;
    }


    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return nameTextField;
    }
}
