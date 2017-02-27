package io.t28.json2java.idea.view;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.util.Collections;
import java.util.stream.Stream;

public class SettingsPanel {
    private final ButtonGroup styleGroup;

    private JPanel centerPanel;
    private JTextField classNameSuffixField;
    private JTextField classNamePrefixField;
    private JRadioButton defaultStyleButton;
    private JRadioButton gsonStyleButton;
    private JRadioButton jacksonStyleButton;
    private JRadioButton moshiStyleButton;

    public SettingsPanel() {
        styleGroup = new ButtonGroup();
        styleGroup.add(defaultStyleButton);
        styleGroup.add(gsonStyleButton);
        styleGroup.add(jacksonStyleButton);
        styleGroup.add(moshiStyleButton);
    }

    @Nonnull
    @CheckReturnValue
    public JComponent getComponent() {
        return centerPanel;
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

    private Stream<JRadioButton> getStyleButtonStream() {
        return Collections.list(styleGroup.getElements())
                .stream()
                .filter(JRadioButton.class::isInstance)
                .map(JRadioButton.class::cast);
    }
}
