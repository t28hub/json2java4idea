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

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public final class Extensions {
    private static final String DELIMITER = ".";

    private Extensions() {
    }

    @Nonnull
    @CheckReturnValue
    public static String remove(@Nonnull String fileName, @Nonnull FileType fileType) {
        final String extension = fileType.getDefaultExtension();
        if (fileName.endsWith(DELIMITER + extension)) {
            return fileName.substring(0, fileName.length() - (extension.length() + 1));
        }
        return fileName;
    }

    @Nonnull
    @CheckReturnValue
    public static String append(@Nonnull String fileName, @Nonnull FileType fileType) {
        final String extension = fileType.getDefaultExtension();
        if (fileName.endsWith(DELIMITER + extension)) {
            return fileName;
        }
        return fileName + DELIMITER + extension;
    }
}
