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

package io.t28.json2java.core.io.exception;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JsonParseException extends IOException {
    private static final long serialVersionUID = 1477109671450199015L;

    public JsonParseException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
