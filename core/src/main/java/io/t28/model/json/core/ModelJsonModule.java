package io.t28.model.json.core;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import io.t28.model.json.core.io.JacksonReader;
import io.t28.model.json.core.io.JavaFileWriter;
import io.t28.model.json.core.io.JavaWriter;
import io.t28.model.json.core.io.JsonReader;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
class ModelJsonModule implements Module {
    private final Context context;

    ModelJsonModule(@Nonnull Context context) {
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
    public ClassFactory provideClassFactory() {
        return new ClassFactory(context);
    }
}
