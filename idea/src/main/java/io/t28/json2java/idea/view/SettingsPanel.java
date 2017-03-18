package io.t28.json2java.idea.view;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import io.t28.json2java.core.Style;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.stream.Stream;

public class SettingsPanel implements Disposable, ActionListener, DocumentListener {
    private static final String PREVIEW_PANEL_NAME = "preview";
    private static final String EMPTY_TEXT = "";
    private static final int INITIAL_OFFSET = 0;
    private static final int PREFERRED_PREVIEW_WIDTH = 1920;
    private static final int PREFERRED_PREVIEW_HEIGHT = -1;

    private final ButtonGroup styleGroup;

    private JPanel rootPanel;
    private JRadioButton defaultStyleButton;
    private JRadioButton gsonStyleButton;
    private JRadioButton jacksonStyleButton;
    private JRadioButton moshiStyleButton;
    private JTextField classNameSuffixField;
    private JTextField classNamePrefixField;
    private JPanel previewPanel;

    private Editor previewEditor;
    private Document previewDocument;

    public SettingsPanel() {
        styleGroup = new ButtonGroup();
        styleGroup.add(defaultStyleButton);
        styleGroup.add(gsonStyleButton);
        styleGroup.add(jacksonStyleButton);
        styleGroup.add(moshiStyleButton);

        Collections.list(styleGroup.getElements()).forEach(button -> button.addActionListener(this));
        classNamePrefixField.getDocument().addDocumentListener(this);
        classNameSuffixField.getDocument().addDocumentListener(this);
    }

    @Override
    public void dispose() {
        Collections.list(styleGroup.getElements()).forEach(button -> button.removeActionListener(this));
        classNamePrefixField.getDocument().removeDocumentListener(this);
        classNameSuffixField.getDocument().removeDocumentListener(this);

        if (previewEditor == null || previewEditor.isDisposed()) {
            return;
        }
        EditorFactory.getInstance().releaseEditor(previewEditor);
    }

    @Override
    public void actionPerformed(@Nonnull ActionEvent event) {
        onChanged();
    }

    @Override
    public void insertUpdate(@Nonnull DocumentEvent event) {
        onChanged();
    }

    @Override
    public void removeUpdate(@Nonnull DocumentEvent event) {
        onChanged();
    }

    @Override
    public void changedUpdate(@Nonnull DocumentEvent event) {
        onChanged();
    }

    @Nonnull
    @CheckReturnValue
    public JComponent getComponent() {
        return rootPanel;
    }

    @Nonnull
    @CheckReturnValue
    public Style getStyle() {
        final ButtonModel selected = styleGroup.getSelection();
        return getStyleButtonStream()
                .filter(button -> {
                    final ButtonModel model = button.getModel();
                    return model.equals(selected);
                })
                .map(button -> {
                    final String name = button.getText();
                    return Style.fromName(name, Style.NONE);
                })
                .findFirst()
                .orElse(Style.NONE);
    }

    public void setStyle(@Nonnull Style style) {
        getStyleButtonStream().forEach(button -> {
            final String text = button.getText();
            button.setSelected(style.name().equalsIgnoreCase(text));
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
        final CommandProcessor processor = CommandProcessor.getInstance();
        processor.executeCommand(null, () -> {
            final Application application = ApplicationManager.getApplication();
            application.runWriteAction(() -> {
                previewDocument.replaceString(INITIAL_OFFSET, previewDocument.getTextLength(), text);

                final int textLength = previewDocument.getTextLength();
                final CaretModel caret = previewEditor.getCaretModel();
                if (caret.getOffset() >= textLength) {
                    caret.moveToOffset(textLength);
                }
            });
        }, null, null);
    }

    @Nonnull
    @CheckReturnValue
    @VisibleForTesting
    String getPreviewText() {
        return previewDocument.getText();
    }

    protected void onChanged() {
    }

    private Stream<JRadioButton> getStyleButtonStream() {
        return Collections.list(styleGroup.getElements())
                .stream()
                .filter(JRadioButton.class::isInstance)
                .map(JRadioButton.class::cast);
    }

    private void createUIComponents() {
        final EditorFactory editorFactory = EditorFactory.getInstance();
        previewDocument = editorFactory.createDocument(EMPTY_TEXT);
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
        previewPanel.setName(PREVIEW_PANEL_NAME);
        previewPanel.setPreferredSize(new Dimension(PREFERRED_PREVIEW_WIDTH, PREFERRED_PREVIEW_HEIGHT));
    }
}
