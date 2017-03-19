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

package io.t28.json2java.idea.util;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class Formatter {
    private static final String NAME = "temporary";

    private final PsiFileFactory fileFactory;
    private final FileType fileType;

    public Formatter(@Nonnull PsiFileFactory fileFactory, @Nonnull FileType fileType) {
        this.fileFactory = fileFactory;
        this.fileType = fileType;
    }

    @NotNull
    @CheckReturnValue
    public String format(@Nonnull String text) {
        final String name = Extensions.append(NAME, fileType);
        final PsiFile file = fileFactory.createFileFromText(name, fileType, text);
        final CodeStyleManager styleManager = CodeStyleManager.getInstance(file.getProject());
        styleManager.reformat(file);
        return file.getText();
    }
}
