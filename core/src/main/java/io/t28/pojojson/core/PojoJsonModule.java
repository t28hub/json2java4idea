package io.t28.pojojson.core;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import io.t28.pojojson.core.io.JacksonParser;
import io.t28.pojojson.core.io.JavaFileBuilder;
import io.t28.pojojson.core.io.JavaBuilder;
import io.t28.pojojson.core.io.JsonParser;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
class PojoJsonModule implements Module {
    private final Context context;

    PojoJsonModule(@Nonnull Context context) {
        this.context = Context.copyOf(context);
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(JsonParser.class)
                .to(JacksonParser.class);
        binder.bind(JavaBuilder.class)
                .to(JavaFileBuilder.class);
    }

    @Nonnull
    @Provides
    public ClassGenerator provideClassFactory() {
        return new ClassGenerator(context);
    }
}
