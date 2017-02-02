package io.t28.pojojson.core;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import io.t28.pojojson.core.io.JacksonReader;
import io.t28.pojojson.core.io.JavaFileWriter;
import io.t28.pojojson.core.io.JavaWriter;
import io.t28.pojojson.core.io.JsonReader;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
class PojoJsonModule implements Module {
    private final Context context;

    PojoJsonModule(@Nonnull Context context) {
        this.context = Context.copyOf(context);
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(JsonReader.class)
                .to(JacksonReader.class);
    }

    @Nonnull
    @Provides
    public JavaWriter provideJavaWriter() {
        return new JavaFileWriter(context.sourceDirectory());
    }

    @Nonnull
    @Provides
    public ClassGenerator provideClassFactory() {
        return new ClassGenerator(context);
    }
}
