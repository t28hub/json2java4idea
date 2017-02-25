package io.t28.pojojson.idea.commands;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.ThrowableComputable;
import io.t28.pojojson.idea.exceptions.JsonFormatException;
import io.t28.pojojson.idea.utils.JsonFormatter;

import javax.annotation.Nonnull;

public class FormatJsonCommand implements ThrowableComputable<String, JsonFormatException> {
    private static final int INITIAL_INDEX = 0;

    private final String json;
    private final Editor editor;
    private final Document document;
    private final JsonFormatter formatter;

    @Inject
    public FormatJsonCommand(@Nonnull @Assisted String json,
                             @Nonnull @Assisted Editor editor,
                             @Nonnull @Assisted Document document,
                             @Nonnull JsonFormatter formatter) {
        this.json = Preconditions.checkNotNull(json);
        this.editor = Preconditions.checkNotNull(editor);
        this.document = Preconditions.checkNotNull(document);
        this.formatter = Preconditions.checkNotNull(formatter);
    }

    @Nonnull
    @Override
    public String compute() throws JsonFormatException {
        final String formatted = formatter.format(json);
        document.replaceString(INITIAL_INDEX, document.getTextLength(), formatted);

        final int textLength = document.getTextLength();
        final CaretModel caret = editor.getCaretModel();
        if (caret.getOffset() >= textLength) {
            caret.moveToOffset(textLength);
        }
        return formatted;
    }
}
