package io.t28.json2java.idea.utils;

import com.google.inject.Inject;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.squareup.javapoet.TypeName;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.stream.Stream;

public class PsiTypeConverter implements Function<TypeName, PsiType> {
    private final PsiManager psiManager;

    @Inject
    public PsiTypeConverter(@Nonnull PsiManager psiManager) {
        this.psiManager = psiManager;
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public PsiType apply(@Nonnull TypeName typeName) {
        return Stream.of(PrimitiveType.values())
                .filter(primitiveType -> primitiveType.isSameAs(typeName))
                .map(PrimitiveType::psiType)
                .findFirst()
                .orElseGet(() -> PsiType.getJavaLangObject(psiManager, GlobalSearchScope.EMPTY_SCOPE));
    }

    @Nonnull
    @CheckReturnValue
    static TypeName box(@Nonnull TypeName typeName) {
        try {
            return typeName.box();
        } catch (Exception e) {
            return typeName;
        }
    }

    @Nonnull
    @CheckReturnValue
    static TypeName unbox(@Nonnull TypeName typeName) {
        try {
            return typeName.unbox();
        } catch (Exception e) {
            return typeName;
        }
    }

    @SuppressWarnings("unused")
    enum PrimitiveType {
        BOOLEAN(PsiType.BOOLEAN, TypeName.BOOLEAN),
        BYTE(PsiType.BYTE, TypeName.BYTE),
        CHAR(PsiType.CHAR, TypeName.CHAR),
        DOUBLE(PsiType.DOUBLE, TypeName.DOUBLE),
        FLOAT(PsiType.FLOAT, TypeName.FLOAT),
        INT(PsiType.INT, TypeName.INT),
        LONG(PsiType.LONG, TypeName.LONG),
        SHORT(PsiType.SHORT, TypeName.SHORT),
        VOID(PsiType.VOID, TypeName.VOID);

        private final PsiType psiType;
        private final TypeName typeName;

        PrimitiveType(@Nonnull PsiType psiType, @Nonnull TypeName typeName) {
            this.psiType = psiType;
            this.typeName = typeName;
        }

        @Nonnull
        @CheckReturnValue
        PsiType psiType() {
            return psiType;
        }

        @CheckReturnValue
        boolean isSameAs(@Nonnull TypeName typeName) {
            final TypeName boxTypeName = box(typeName);
            final TypeName unboxTypeName = unbox(typeName);
            return this.typeName.equals(boxTypeName) || this.typeName.equals(unboxTypeName);
        }
    }
}
