package io.t28.json2java.idea.utils;

import com.google.inject.Inject;
import com.intellij.json.JsonFileType;
import com.intellij.psi.PsiFileFactory;

import javax.annotation.Nonnull;

public class JsonFormatter extends Formatter {
    @Inject
    public JsonFormatter(@Nonnull PsiFileFactory fileFactory) {
        super(fileFactory, JsonFileType.INSTANCE);
    }
}
