package io.t28.model.json.core;

import com.google.common.io.Files;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.builder.BuilderType;
import io.t28.model.json.core.json.JsonValue;
import io.t28.model.json.core.parser.JacksonParser;
import io.t28.model.json.core.parser.JsonParser;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class ModelJson {
    public static void main(String[] args) throws Exception {
        final File file = new File("core/src/main/resources/repositories.json");
        final String json = Files.toString(file, StandardCharsets.UTF_8);
        final Context context = Context.builder()
                .builderType(BuilderType.GSON)
                .build();
        final ModelJson modelJson = new ModelJson(context);
        final String java = modelJson.generate("io.t28.mode.json.example", "Repository", json);
        System.out.println(java);
    }

    private final Context context;

    public ModelJson(@Nonnull Context context) {
        this.context = Context.copyOf(context);
    }

    @Nonnull
    public String generate(@Nonnull String packageName, @Nonnull String className, @Nonnull String json) {
        final JsonParser parser = new JacksonParser();
        final JsonValue value = parser.parse(json);

        final ClassFactory factory = new ClassFactory(context);
        final TypeSpec created = factory.create(className, value);

        final JavaFile javaFile = JavaFile.builder(packageName, created)
                .indent(context.indent())
                .skipJavaLangImports(true)
                .build();
        return javaFile.toString();
    }
}
