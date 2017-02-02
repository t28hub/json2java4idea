package io.t28.pojojson.core.utils;

import com.google.common.collect.ImmutableSet;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.util.Set;

public class Keywords {
    private static final Set<String> RESERVED = ImmutableSet.<String>builder()
            .add("abstract")
            .add("assert")
            .add("boolean")
            .add("break")
            .add("byte")
            .add("case")
            .add("catch")
            .add("char")
            .add("class")
            .add("const")
            .add("continue")
            .add("default")
            .add("do")
            .add("double")
            .add("else")
            .add("enum")
            .add("extends")
            .add("false")
            .add("final")
            .add("finally")
            .add("float")
            .add("for")
            .add("goto")
            .add("if")
            .add("implements")
            .add("import")
            .add("instanceof")
            .add("int")
            .add("interface")
            .add("long")
            .add("native")
            .add("new")
            .add("null")
            .add("package")
            .add("private")
            .add("protected")
            .add("public")
            .add("return")
            .add("short")
            .add("static")
            .add("strictfp")
            .add("super")
            .add("switch")
            .add("synchronized")
            .add("this")
            .add("throw")
            .add("throws")
            .add("transient")
            .add("try")
            .add("true")
            .add("void")
            .add("volatile")
            .add("while")
            .build();

    private Keywords() {
    }

    @CheckReturnValue
    public static boolean isReserved(@Nullable String keyword) {
        return RESERVED.contains(keyword);
    }
}
