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

package io.t28.json2java.idea.validator;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiNameHelper;
import io.t28.json2java.idea.Json2JavaBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClassPrefixValidator implements InputValidatorEx {
    private final Json2JavaBundle bundle;
    private final PsiNameHelper nameHelper;

    @Inject
    public ClassPrefixValidator(@NotNull Json2JavaBundle bundle, @NotNull PsiNameHelper nameHelper) {
        this.bundle = bundle;
        this.nameHelper = nameHelper;
    }

    @Nullable
    @Override
    public String getErrorText(@Nullable String prefix) {
        if (canClose(prefix)) {
            return null;
        }
        return bundle.message("error.message.validator.class.prefix", prefix);
    }

    @Override
    public boolean checkInput(@Nullable String prefix) {
        return true;
    }

    @Override
    public boolean canClose(@Nullable String prefix) {
        if (Strings.isNullOrEmpty(prefix)) {
            return true;
        }

        if (!nameHelper.isIdentifier(prefix)) {
            return false;
        }

        final char first = prefix.charAt(0);
        return StringUtil.isJavaIdentifierStart(first);
    }
}
