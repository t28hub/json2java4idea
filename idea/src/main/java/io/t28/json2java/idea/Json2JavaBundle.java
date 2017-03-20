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

package io.t28.json2java.idea;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.PropertyKey;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class Json2JavaBundle extends AbstractBundle {
    private static final String BUNDLE = "messages.Json2Java4IdeaBundle";

    public Json2JavaBundle() {
        super(BUNDLE);
    }

    @Nonnull
    public static Json2JavaBundle getInstance() {
        return new Json2JavaBundle();
    }

    @Nonnull
    @CheckReturnValue
    public String message(@Nonnull @PropertyKey(resourceBundle = BUNDLE) String key, @Nonnull Object... objects) {
        return getMessage(key, objects);
    }
}
