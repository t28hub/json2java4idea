package io.t28.pojojson.idea.utils;

import org.jetbrains.annotations.NotNull;

public class Tuple {

    @NotNull
    public static <T1, T2, T3> Tuple3<T1, T2, T3> tuple(@NotNull T1 value1, @NotNull T2 value2, @NotNull T3 value3) {
        return new Tuple3<>(value1, value2, value3);
    }

}
