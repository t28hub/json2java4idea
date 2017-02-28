package io.t28.json2java.idea.view;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.util.Collections;
import java.util.stream.Stream;

public class SettingsPanel implements Disposable {
    private final ButtonGroup styleGroup;

    private JPanel rootPanel;
    private JTextField classNameSuffixField;
    private JTextField classNamePrefixField;
    private JRadioButton defaultStyleButton;
    private JRadioButton gsonStyleButton;
    private JRadioButton jacksonStyleButton;
    private JRadioButton moshiStyleButton;
    private JPanel previewPanel;

    private Editor previewEditor;
    private Document previewDocument;

    public SettingsPanel() {
        styleGroup = new ButtonGroup();
        styleGroup.add(defaultStyleButton);
        styleGroup.add(gsonStyleButton);
        styleGroup.add(jacksonStyleButton);
        styleGroup.add(moshiStyleButton);

    }

    @Override
    public void dispose() {
        EditorFactory.getInstance().releaseEditor(previewEditor);
    }

    @Nonnull
    @CheckReturnValue
    public JComponent getComponent() {
        return rootPanel;
    }

    @Nonnull
    @CheckReturnValue
    public String getClassStyle() {
        final ButtonModel selected = styleGroup.getSelection();
        return getStyleButtonStream()
                .filter(button -> {
                    final ButtonModel model = button.getModel();
                    return model.equals(selected);
                })
                .map(JRadioButton::getText)
                .findFirst()
                .orElse("");
    }

    public void setClassStyle(@Nonnull String style) {
        getStyleButtonStream().forEach(button -> {
            final String text = button.getText();
            button.setSelected(style.equalsIgnoreCase(text));
        });
    }

    @Nonnull
    @CheckReturnValue
    public String getClassNamePrefix() {
        return classNamePrefixField.getText().trim();
    }

    public void setClassNamePrefix(@Nonnull String prefix) {
        classNamePrefixField.setText(prefix);
    }

    @Nonnull
    @CheckReturnValue
    public String getClassNameSuffix() {
        return classNameSuffixField.getText().trim();
    }

    public void setClassNameSuffix(@Nonnull String suffix) {
        classNameSuffixField.setText(suffix);
    }

    public void setPreviewText(@Nonnull String text) {
        previewDocument.replaceString(0, previewDocument.getTextLength(), text);

        final int textLength = previewDocument.getTextLength();
        final CaretModel caret = previewEditor.getCaretModel();
        if (caret.getOffset() >= textLength) {
            caret.moveToOffset(textLength);
        }
    }

    private Stream<JRadioButton> getStyleButtonStream() {
        return Collections.list(styleGroup.getElements())
                .stream()
                .filter(JRadioButton.class::isInstance)
                .map(JRadioButton.class::cast);
    }

    private void createUIComponents() {
        final EditorFactory editorFactory = EditorFactory.getInstance();
        previewDocument = editorFactory.createDocument("");
        previewEditor = editorFactory.createEditor(previewDocument, null, JavaFileType.INSTANCE, true);

        final EditorSettings settings = previewEditor.getSettings();
        settings.setWhitespacesShown(true);
        settings.setLineMarkerAreaShown(false);
        settings.setIndentGuidesShown(false);
        settings.setLineNumbersShown(false);
        settings.setFoldingOutlineShown(false);
        settings.setRightMarginShown(false);
        settings.setVirtualSpace(false);
        settings.setWheelFontChangeEnabled(false);
        settings.setUseSoftWraps(false);
        settings.setAdditionalColumnsCount(0);
        settings.setAdditionalLinesCount(1);

        previewPanel = (JPanel) previewEditor.getComponent();
        previewPanel.setPreferredSize(new Dimension(1920, -1));
    }
}
