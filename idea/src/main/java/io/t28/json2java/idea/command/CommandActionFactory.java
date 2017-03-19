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

package io.t28.json2java.idea.command;

import com.google.inject.assistedinject.Assisted;
import com.intellij.psi.PsiDirectory;
import io.t28.json2java.core.JavaConverter;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface CommandActionFactory {
    @Nonnull
    @CheckReturnValue
    NewClassCommandAction create(
            @Nonnull @Assisted("Name") String name,
            @Nonnull @Assisted("Json") String json,
            @Nonnull PsiDirectory directory,
            @Nonnull JavaConverter converter
    );
}
