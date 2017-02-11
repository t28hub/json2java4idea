package io.t28.pojojson.idea.commands;

import com.google.common.base.Preconditions;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class SetTextCommand implements Runnable {
    private static final int INITIAL_INDEX = 0;

    private final String text;
    private final Editor editor;
    private final Document document;

    private SetTextCommand(@Nonnull Builder builder) {
        text = Preconditions.checkNotNull(builder.text);
        editor = Preconditions.checkNotNull(builder.editor);
        document = Preconditions.checkNotNull(builder.document);
    }

    @Nonnull
    @CheckReturnValue
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void run() {
        document.replaceString(INITIAL_INDEX, document.getTextLength(), text);

        final int textLength = document.getTextLength();
        final CaretModel caret = editor.getCaretModel();
        if (caret.getOffset() >= textLength) {
            caret.moveToOffset(textLength);
        }
    }

    public static class Builder {
        private String text;
        private Editor editor;
        private Document document;

        private Builder() {
        }

        @Nonnull
        @CheckReturnValue
        public Builder text(@Nonnull String text) {
            this.text = text;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder editor(@Nonnull Editor editor) {
            this.editor = editor;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder document(@Nonnull Document document) {
            this.document = document;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public SetTextCommand build() {
            return new SetTextCommand(this);
        }
    }
}
