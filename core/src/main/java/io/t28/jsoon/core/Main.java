package io.t28.jsoon.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.squareup.javapoet.JavaFile;
import io.t28.jsoon.core.builder.ClassBuilder;
import io.t28.jsoon.core.builder.ModelClassBuilder;
import io.t28.jsoon.core.json.JsonElement;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        final File file = new File("core/src/main/resources/issue.json");
        final String json = Files.toString(file, StandardCharsets.UTF_8);
        final ObjectMapper mapper = new ObjectMapper();

        final ClassBuilder builder = new ModelClassBuilder("Example", Modifier.PUBLIC);
        final JsonVisitor visitor = new JsonVisitor(builder);
        final JsonElement root = JsonElement.wrap(mapper.readTree(json));
        if (root.isObject()) {
            root.asObject()
                    .stream()
                    .forEach(child -> child.accept(visitor));
        } else if (root.isArray()) {
            root.accept(visitor);
        }

        final JavaFile javaFile = JavaFile.builder("io.t28.jsoon.core", builder.build())
                .indent("    ")
                .skipJavaLangImports(true)
                .build();
        System.out.println(javaFile.toString());
    }
}
