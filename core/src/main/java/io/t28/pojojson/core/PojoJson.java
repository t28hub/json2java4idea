package io.t28.pojojson.core;

import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.squareup.javapoet.TypeSpec;
import io.t28.pojojson.core.io.JavaWriter;
import io.t28.pojojson.core.io.JsonReader;
import io.t28.pojojson.core.json.JsonValue;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PojoJson {
    @Inject
    @SuppressWarnings("unused")
    private JsonReader jsonReader;

    @Inject
    @SuppressWarnings("unused")
    private JavaWriter javaWriter;

    @Inject
    @SuppressWarnings("unused")
    private ClassGenerator generator;

    public PojoJson(@Nonnull Context context) {
        final Injector injector = Guice.createInjector(new PojoJsonModule(context));
        injector.injectMembers(this);
    }

    @Nonnull
    public void generate(@Nonnull String packageName, @Nonnull String className, @Nonnull String json) throws IOException {
        final JsonValue value = jsonReader.read(json);
        final TypeSpec typeSpec = generator.generate(className, value);
        javaWriter.write(packageName, typeSpec);
    }

    public static void main(String[] args) throws Exception {
        final File file = new File("core/src/main/resources/repositories.json");
        final String json = Files.toString(file, StandardCharsets.UTF_8);
        final Context context = Context.builder()
                .sourceDirectory(new File("core/build/classes/main/generated"))
                .style(ClassStyle.GSON)
                .build();
        final PojoJson pojoJson = new PojoJson(context);
        pojoJson.generate("io.t28.mode.json.example", "Repository", json);
    }
}
