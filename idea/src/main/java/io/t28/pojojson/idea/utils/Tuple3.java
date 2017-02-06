package io.t28.pojojson.idea.utils;

import org.jetbrains.annotations.NotNull;

public class Tuple3<T1, T2, T3> {
    private final T1 value1;
    private final T2 value2;
    private final T3 value3;

    Tuple3(@NotNull T1 value1, @NotNull T2 value2, @NotNull T3 value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    @NotNull
    public T1 value1() {
        return value1;
    }

    @NotNull
    public T2 value2() {
        return value2;
    }

    @NotNull
    public T3 value3() {
        return value3;
    }
}
