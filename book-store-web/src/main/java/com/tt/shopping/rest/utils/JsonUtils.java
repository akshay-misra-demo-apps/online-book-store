package com.tt.shopping.rest.utils;

import org.openapitools.jackson.nullable.JsonNullable;
import java.util.function.Consumer;

public class JsonUtils {

    private JsonUtils() {}

    public static <T> void changeIfPresent(JsonNullable<T> nullable, Consumer<T> consumer) {
        if (nullable.isPresent()) {
            consumer.accept(nullable.get());
        }
    }
}
