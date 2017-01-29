package io.t28.model.json.core;

import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.builder.BuilderType;
import io.t28.model.json.core.io.JavaWriter;
import io.t28.model.json.core.io.JsonReader;
import io.t28.model.json.core.json.JsonValue;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ModelJson {
    @Inject
    @SuppressWarnings("unused")
    private JsonReader jsonReader;

    @Inject
    @SuppressWarnings("unused")
    private JavaWriter javaWriter;

    @Inject
    @SuppressWarnings("unused")
    private ClassFactory classFactory;

    public ModelJson(@Nonnull Context context) {
        final Injector injector = Guice.createInjector(new ModelJsonModule(context));
        injector.injectMembers(this);
    }

    @Nonnull
    public void generate(@Nonnull String packageName, @Nonnull String className, @Nonnull String json) throws IOException {
        final JsonValue value = jsonReader.read(json);
        final TypeSpec typeSpec = classFactory.create(className, value);
        javaWriter.write(packageName, typeSpec);
    }

    public static void main(String[] args) throws Exception {
        final File file = new File("core/src/main/resources/repositories.json");
        final String json = Files.toString(file, StandardCharsets.UTF_8);
        final Context context = Context.builder()
                .sourceDirectory(new File("core/build/classes/main/generated"))
                .builderType(BuilderType.GSON)
                .build();
        final ModelJson modelJson = new ModelJson(context);
        modelJson.generate("io.t28.mode.json.example", "Repository", json);
    }
}
